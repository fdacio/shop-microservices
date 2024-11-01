package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.ProductNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import br.com.daciosoftware.shop.models.dto.product.ProductDTO;
import br.com.daciosoftware.shop.models.dto.order.ItemDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Value("${product.api.url}")
    private String productApiURL;

    public List<ItemDTO> findItens(OrderDTO shopDTO) {

        List<ItemDTO> itensDTO = new ArrayList<>();

        WebClient webClient = WebClient.builder().baseUrl(productApiURL).build();

        for (ItemDTO i : shopDTO.getItens()) {

            Long productId = i.getProduct().getId();
            Mono<ProductDTO> product = webClient.get().uri("/product/" + productId)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                                case 404 -> Mono.error(new ProductNotFoundException());
                                default -> Mono.error(new ShopGenericException("Erro no microsserviço product"));
                            }).bodyToMono(ProductDTO.class);

            ProductDTO productDTO = product.block();
            i.setProduct(productDTO);
            i.setPreco(productDTO != null ? productDTO.getPreco() : 0);
            itensDTO.add(i);

        }

        return itensDTO;
    }
}
