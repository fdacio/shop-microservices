package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserInvalidKeyTokenException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserUsernameExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceAuthUnavailableException;
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

            Mono<AuthUserDTO> authUser = webClient
                    .get()
                    .uri("/auth/user/" + keyToken + "/key-token")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 409 -> Mono.error(new AuthUserInvalidKeyTokenException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default -> Mono.error(new RuntimeException());
                            })
                    .bodyToMono(AuthUserDTO.class);

            return Optional.ofNullable(authUser.block());

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new ServiceAuthUnavailableException();
            } else {
                throw exception;
            }
        }
    }

    public AuthUserDTO createAuthUser(CreateAuthUserDTO createAuthUserDTO) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(authApiURL)
                    .build();

            Mono<AuthUserDTO> user = webClient
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
                    .bodyToMono(AuthUserDTO.class);

            return user.block();

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new ServiceAuthUnavailableException();
            } else {
                throw exception;
            }
        }

    }
}
