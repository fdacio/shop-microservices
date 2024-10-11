package br.com.daciosoftware.shop.shopping.service;

import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import org.springframework.beans.factory.annotation.Value;
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

        try {

            WebClient webClient = WebClient.builder()
                    .baseUrl(authApiURL)
                    .build();

            Mono<AuthUserDTO> user = webClient
                    .get()
                    .uri("/auth/user/authenticated/" + token)
                    .retrieve()
                    .bodyToMono(AuthUserDTO.class);

            return user.block();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
