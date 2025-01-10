package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceProductUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.product.ProductNotFoundException;
import br.com.daciosoftware.shop.models.dto.order.ItemDTO;
import br.com.daciosoftware.shop.models.dto.product.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductService {

    @Value("${product.api.url}")
    private String productApiURL;

    public List<ItemDTO> findItens(List<ItemDTO> itensDTO) {

        try {

            WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

            for (ItemDTO item : itensDTO) {

                Long productId = item.getProduct().getId();

                Mono<ProductDTO> product = webClient.get()
                        .uri("/product/" + productId)
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::isError,
                                response -> switch (response.statusCode().value()) {
                                    case 404 -> Mono.error(new ProductNotFoundException());
                                    case 401 -> Mono.error(new AuthUnauthorizedException());
                                    case 403 -> Mono.error(new AuthForbiddenException());
                                    default -> Mono.error(new RuntimeException());
                                })
                        .bodyToMono(ProductDTO.class);

                ProductDTO productDTO = product.block();
                item.setProduct(productDTO);
                item.setPreco(productDTO != null ? productDTO.getPreco() : 0);
                itensDTO.add(item);
            }
            return itensDTO;

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceProductUnavailableException();
            } else {
                throw exception;
            }
        }
    }
}
