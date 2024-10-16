package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.CategoryNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.product.CategoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Value("${product.api.url}")
    private String productApiURL;

    public Set<CategoryDTO> findCategorysByUser(CustomerDTO userDTO) {

        Set<CategoryDTO> categorysDTO = new HashSet<>();

        if (userDTO.getInteresses() != null) {

            try {

                WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

                for (CategoryDTO c : userDTO.getInteresses()) {
                    Long categoryId = c.getId();
                    Mono<CategoryDTO> category = webClient
                            .get()
                            .uri("/category/" + categoryId)
                            .retrieve()
                            .onStatus(
                                    HttpStatusCode::isError,
                                    response -> switch (response.statusCode().value()) {
                                        case 401, 403 -> Mono.error(new AuthUnauthorizedException());
                                        case 404 -> Mono.error(new CategoryNotFoundException());
                                        default ->
                                                Mono.error(new ShopGenericException("Erro no microsserviço product"));
                                    })
                            .bodyToMono(CategoryDTO.class);
                    categorysDTO.add(category.block());

                }
            } catch (Exception e) {
                throw new ShopGenericException("Erro no microsserviço product");
            }
        }

        return categorysDTO;
    }

    public List<CategoryDTO> findAll() {

        WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

        try {

            Mono<List<CategoryDTO>> categorys = webClient
                    .get()
                    .uri("/category")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 401, 403 -> Mono.error(new AuthUnauthorizedException());
                                case 404 -> Mono.error(new CategoryNotFoundException());
                                default -> Mono.error(new ShopGenericException("Erro no microsserviço product"));
                            })
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return categorys.block();

        } catch (Exception e) {
            throw new ShopGenericException("Erro no microsserviço product");
        }
    }
}
