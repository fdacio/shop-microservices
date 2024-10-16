package br.com.daciosoftware.shop.shopping.service;

import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    @Value("${auth.api.url}")
    private String authApiURL;

    @Transactional
    public AuthUserDTO getUserAuthenticated(String token) {

        WebClient webClient = WebClient.builder()
                .baseUrl(authApiURL)
                .build();

        Mono<AuthUserDTO> user = webClient
                .post()
                .uri("/auth/user/authenticated")
                .header("Authorization", token)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> switch (response.statusCode().value()) {
                            case 401, 403 -> Mono.error(new AuthUnauthorizedException());
                            case 404 -> Mono.error(new AuthUserNotFoundException());
                            default -> Mono.error(new ShopGenericException("Erro no microsserviço auth"));
                        })
                .bodyToMono(AuthUserDTO.class);

        return user.block();


    }
}
