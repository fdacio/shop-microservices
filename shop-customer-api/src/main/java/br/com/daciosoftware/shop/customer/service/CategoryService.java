package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceAuthUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.product.CategoryNotFoundException;
import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class CategoryService {

    @Value("${product.api.url}")
    private String productApiURL;

    public Set<CategoryDTO> validCategorys(Set<CategoryDTO> categorysDTO) {

        if (categorysDTO != null) {

            try {
                WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

                for (CategoryDTO c : categorysDTO) {
                    Long categoryId = c.getId();
                    CategoryDTO category = webClient
                            .get()
                            .uri("/category/" + categoryId)
                            .retrieve()
                            .onStatus(
                                    HttpStatusCode::isError,
                                    response -> switch (response.statusCode().value()) {
                                        case 404 -> Mono.error(new CategoryNotFoundException());
                                        case 401 -> Mono.error(new AuthUnauthorizedException());
                                        case 403 -> Mono.error(new AuthForbiddenException());
                                        default -> Mono.error(new RuntimeException());
                                    })
                            .bodyToMono(CategoryDTO.class)
                            .block();

                    categorysDTO.add(category);

                }

            } catch (Exception exception) {
                if (exception instanceof WebClientRequestException) {
                    throw new MicroserviceAuthUnavailableException();
                } else {
                    throw exception;
                }
            }

        }

        return categorysDTO;
    }

    public Flux<CategoryDTO> findAll() {

        try {

            WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

            return webClient
                    .get()
                    .uri("/category")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default -> Mono.error(new RuntimeException());
                            })
                    .bodyToFlux(CategoryDTO.class);

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceAuthUnavailableException();
            } else {
                throw exception;
            }
        }

    }

    public CategoryDTO findById(Long id) {

        try {

            WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

            return webClient
                    .get()
                    .uri("/category/" + id)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 404 -> Mono.error(new CategoryNotFoundException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default -> Mono.error(new RuntimeException());
                            })
                    .bodyToMono(CategoryDTO.class)
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
