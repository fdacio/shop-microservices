package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceAuthUnavailableException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceCustomerUnavailableException;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Value("${customer.api.url}")
    private String customerApiURL;

    public CustomerDTO validCustomerKeyAuth(String customerKeyAuth) {

        try {
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
                                case 409 -> Mono.error(new CustomerInvalidKeyException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default -> Mono.error(new RuntimeException());
                            })
                    .bodyToMono(CustomerDTO.class);

            return user.block();

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new ServiceCustomerUnavailableException();
            } else {
                throw exception;
            }
        }

    }

}
