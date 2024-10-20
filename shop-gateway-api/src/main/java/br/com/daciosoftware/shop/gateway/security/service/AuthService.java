package br.com.daciosoftware.shop.gateway.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    @Value("${auth.api.url}")
    private String authApiURL;

    public String getContentPublicKey() {

        WebClient webClient = WebClient.builder().baseUrl(authApiURL).build();

        Mono<String> mono = webClient
                .get()
                .uri("/auth/token/public-key")
                .retrieve()
                .bodyToMono(String.class);

        return mono.block();
    }

    public String[] getRules() {
        WebClient webClient = WebClient.builder().baseUrl(authApiURL).build();

        Mono<String[]> mono = webClient
                .get()
                .uri("/auth/rule")
                .retrieve()
                .bodyToMono(String[].class);

        return mono.block();

    }

}
