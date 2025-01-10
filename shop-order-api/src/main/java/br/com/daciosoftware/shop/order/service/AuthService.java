package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceAuthUnavailableException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserKeyTokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    @Value("${auth.api.url}")
    private String authApiURL;

    @Transactional
    public AuthUserKeyTokenDTO getUserAuthenticated(String token) {

        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(authApiURL)
                    .build();

            Mono<AuthUserKeyTokenDTO> user = webClient
                    .post()
                    .uri("/auth/user/authenticated")
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 404 -> Mono.error(new AuthUserNotFoundException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default -> Mono.error(new RuntimeException());
                            })
                    .bodyToMono(AuthUserKeyTokenDTO.class);

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
