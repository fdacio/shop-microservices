package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.AuthUserNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserKeyTokenDTO;
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
    public AuthUserKeyTokenDTO getUserAuthenticated(String token) {

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
                            case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                            case 404 -> Mono.error(new AuthUserNotFoundException());
                            default -> Mono.error(new ShopGenericException("Erro no microsserviço auth"));
                        })
                .bodyToMono(AuthUserKeyTokenDTO.class);

        return user.block();


    }
}
