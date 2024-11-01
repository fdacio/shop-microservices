package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserInvalidKeyTokenException;
import br.com.daciosoftware.shop.exceptions.exceptions.auth.AuthUserUsernameExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
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
        try {
            Mono<AuthUserDTO> authUser = webClient
                    .get()
                    .uri("/auth/user/" + keyToken + "/key-token")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                                case 404 -> Mono.error(new AuthUserInvalidKeyTokenException());
                                default -> Mono.error(new ShopGenericException("Erro no microsserviço auth"));
                            })
                    .bodyToMono(AuthUserDTO.class);

            return authUser.block();
        } catch (Exception e) {
            throw new ShopGenericException("Erro no microsserviço auth");
        }
    }

    @Transactional
    public AuthUserDTO createAuthUser(CreateAuthUserDTO createAuthUserDTO) {

        WebClient webClient = WebClient.builder()
                .baseUrl(authApiURL)
                .build();
        try {
            Mono<AuthUserDTO> user = webClient
                    .post()
                    .uri("/auth/user/customer")
                    .bodyValue(createAuthUserDTO)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> {
                                System.err.println(response.statusCode().value());
                                switch (response.statusCode().value()) {
                                    case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                                    case 409 -> Mono.error(new AuthUserUsernameExistsException());
                                    default -> Mono.error(new ShopGenericException("Erro no microsserviço auth"));
                                }
                                return Mono.empty();
                            })
                    .bodyToMono(AuthUserDTO.class);

            return user.block();
        } catch (Exception e) {
            throw new ShopGenericException("Erro no microsserviço auth");
        }

    }
}
