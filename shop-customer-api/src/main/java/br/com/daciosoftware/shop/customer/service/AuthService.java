package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserUsernameExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceAuthUnavailableException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

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
                                int status = response.statusCode().value();
                                String message = "Erro ao criar usuário no Auth API - status: " + status;
                                return response.bodyToMono(String.class)
                                        .doOnNext(body -> log.error(message))
                                        .then(Mono.error(
                                                switch (status) {
                                                    case 409 -> new AuthUserUsernameExistsException();
                                                    case 401 -> new AuthUnauthorizedException();
                                                    case 403 -> new AuthForbiddenException();
                                                    default -> new IllegalArgumentException();
                                                }
                                        ));
                            }
                    )
                    .bodyToMono(AuthUserDTO.class)
                    .block(); //aguarda a resposta

        } catch (Exception exception) {

            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceAuthUnavailableException();
            } else {
                throw new RuntimeException();
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
                                int status = response.statusCode().value();
                                return response.bodyToMono(String.class)
                                        .doOnNext(body -> log.error("Erro delete user {}: {}", status, body))
                                        .then(Mono.error(
                                                switch (status) {
                                                    case 409 -> new AuthUserUsernameExistsException();
                                                    case 401 -> new AuthUnauthorizedException();
                                                    case 403 -> new AuthForbiddenException();
                                                    default ->
                                                            new RuntimeException("Erro inesperado do Auth API (status " + status + ")");
                                                }
                                        ));
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is2xxSuccessful,
                            response -> Mono.empty())
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
