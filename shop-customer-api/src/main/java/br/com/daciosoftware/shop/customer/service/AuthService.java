package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserIntegrityViolationException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserUsernameExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceAuthUnavailableException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AuthService {

    @Value("${auth.api.url}")
    private String authApiURL;

    public Optional<AuthUserDTO> findAuthUserByKeyToken(String keyToken) {

        try {

            WebClient webClient = WebClient.builder()
                    .baseUrl(authApiURL)
                    .build();

            AuthUserDTO authUser = webClient
                    .get()
                    .uri("/auth/user/" + keyToken + "/key-token")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 404 -> Mono.error(new AuthUserNotFoundException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default -> Mono.error(new RuntimeException());
                            })
                    .bodyToMono(AuthUserDTO.class)
                    .block();

            return Optional.ofNullable(authUser);

        } catch (RuntimeException exception) {
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceAuthUnavailableException();
            }
            return Optional.empty();
        }
    }

    public AuthUserDTO createAuthUser(CreateAuthUserDTO createAuthUserDTO) {

        try {

            WebClient webClient = WebClient.builder()
                    .baseUrl(authApiURL)
                    .build();

            return webClient
                    .post()
                    .uri("/auth/user/customer")
                    .bodyValue(createAuthUserDTO)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> {
                                switch (response.statusCode().value()) {
                                    case 409 -> Mono.error(new AuthUserUsernameExistsException());
                                    case 401 -> Mono.error(new AuthUnauthorizedException());
                                    case 403 -> Mono.error(new AuthForbiddenException());
                                    default -> Mono.error(new RuntimeException());
                                }
                                return Mono.empty();
                            })
                    .bodyToMono(AuthUserDTO.class)
                    .block();

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceAuthUnavailableException();
            } else {
                throw exception;
            }
        }

    }

    public void deleteAuthUser(AuthUserDTO authUserDTO) {
        try {
            Long id = authUserDTO.getId();
            WebClient webClient = WebClient.builder()
                    .baseUrl(authApiURL)
                    .build();

            webClient
                    .delete()
                    .uri("/auth/user/" + id)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> {
                                switch (response.statusCode().value()) {
                                    case 409 -> Mono.error(new AuthUserIntegrityViolationException());
                                    case 401 -> Mono.error(new AuthUnauthorizedException());
                                    case 403 -> Mono.error(new AuthForbiddenException());
                                    default -> Mono.error(new RuntimeException());
                                }
                                System.out.println("**** Removing de user fail: " + response.statusCode());
                                return Mono.empty();
                            })
                    .onStatus(
                            HttpStatusCode::is2xxSuccessful,
                            response -> {
                                System.out.println("**** Removing de user success: " + response.statusCode());
                                return Mono.empty();
                            })
                    .bodyToMono(Void.class)
                    .block();

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceAuthUnavailableException();
            } else {
                throw exception;
            }
        }

    }
}
