package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.InvalidUserKeyException;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    @Value("${auth.api.url}")
    private String authApiURL;

    public CreateAuthUserDTO createAuthUser(CreateAuthUserDTO createAuthUserDTO) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(authApiURL)
                    .build();
            Mono<CreateAuthUserDTO> user = webClient
                    .post()
                    .uri("/customer/valid")
                    .bodyValue(createAuthUserDTO)
                    .retrieve()
                    .bodyToMono(CreateAuthUserDTO.class);
            return user.block();

        } catch (Exception e) {
            throw new InvalidUserKeyException();
        }
    }
}
