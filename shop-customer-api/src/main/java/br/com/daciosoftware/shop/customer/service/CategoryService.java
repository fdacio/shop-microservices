package br.com.daciosoftware.shop.customer.service;

import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import br.com.daciosoftware.shop.exceptions.exceptions.product.CategoryNotFoundException;
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

    public Set<CategoryDTO> findCategorysByUser(CustomerDTO userDTO) throws CategoryNotFoundException, ShopGenericException {

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
                                        case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                                        case 404 -> Mono.error(new CategoryNotFoundException());
                                        default -> Mono.error(new ShopGenericException("Erro no microsserviço product"));
                                    })
                            .bodyToMono(CategoryDTO.class);

                    categorysDTO.add(category.block());

                }
            } catch (RuntimeException e) {
                throw new ShopGenericException("Microsserviço product não disponível");
            }

        }

        return categorysDTO;
    }

    public List<CategoryDTO> findAll() throws ShopGenericException {
        try {
            WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

            Mono<List<CategoryDTO>> categorys = webClient
                    .get()
                    .uri("/category")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                                default -> Mono.error(new ShopGenericException("Erro no microsserviço product"));
                            })
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });

            return categorys.block();
        } catch (RuntimeException e) {
            throw new ShopGenericException("Microsserviço product não disponível");
        }
    }

    public CategoryDTO findById(Long id) throws CategoryNotFoundException, ShopGenericException {

        try {

            WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

            Mono<CategoryDTO> category = webClient
                    .get()
                    .uri("/category/" + id.toString())
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                                case 404 -> Mono.error(new CategoryNotFoundException());
                                default -> Mono.error(new ShopGenericException("Erro no microsserviço product"));
                            })
                    .bodyToMono(CategoryDTO.class);

            return category.block();

        } catch (RuntimeException e) {
            if (e instanceof CategoryNotFoundException) {
                throw new CategoryNotFoundException();
            }
            throw new ShopGenericException(e.getMessage());
        }

    }

}
