package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.*;
import br.com.daciosoftware.shop.models.dto.auth.AuthUserDTO;
import br.com.daciosoftware.shop.models.dto.auth.CreateAuthUserDTO;
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

    public AuthUserDTO findAuthUserByKeyToken(String keyToken) {
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
                            case 401, 403 -> Mono.error(new AuthUnAuthorizedException());
                            case 404 -> Mono.error(new AuthUserInvalidKeyTokenException());
                            default -> Mono.error(new ShopGenericException("Erro no microsserviço customer"));
                        })
                .bodyToMono(AuthUserDTO.class);

        return authUser.block();
    }

    @Transactional
    public AuthUserDTO createAuthUser(CreateAuthUserDTO createAuthUserDTO) {

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
                            ErrorDTO errorDTO = response.bodyToMono(ErrorDTO.class).block();
                            if (errorDTO != null) {
                                if (errorDTO.getMessage().toLowerCase().contains("username")) {
                                    return Mono.error(new AuthUserUsernameExistsException());
                                }
                                return Mono.error(new ShopGenericException(errorDTO.getMessage()));

                            } else {
                                return Mono.error(new ShopGenericException("Error no microsserviço auth"));
                            }
                        }
                )
                .bodyToMono(AuthUserDTO.class);

        return user.block();


    }
}
