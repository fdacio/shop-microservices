package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Value("${customer.api.url}")
    private String customerApiURL;

    public CustomerDTO validCustomerKeyAuth(String customerKeyAuth) {

        WebClient webClient = WebClient.builder()
                .baseUrl(customerApiURL)
                .build();

        Mono<CustomerDTO> user = webClient
                .post()
                .uri("/customer/valid-key-auth")
                .header("customerKeyAuth", customerKeyAuth)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> switch (response.statusCode().value()) {
                            case 401, 403 -> Mono.error(new ShopGenericException("Recurso não autorizado"));
                            case 404 -> Mono.error(new CustomerInvalidKeyException());
                            default -> Mono.error(new ShopGenericException("Erro no microsserviço customer"));
                        })
                .bodyToMono(CustomerDTO.class);

        return user.block();

    }

}
