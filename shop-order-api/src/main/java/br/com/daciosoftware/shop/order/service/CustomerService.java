package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceCustomerUnavailableException;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    public CustomerDTO validCustomerKeyAuth(String customerKeyAuth) {

        log.info("validCustomerKeyAuth: {}", customerKeyAuth);

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
                            response -> {
                                log.info("Error valid-key-auth: {}", response.statusCode().value());
                                switch (response.statusCode().value()) {
                                    case 409 -> Mono.error(new CustomerInvalidKeyException());
                                    case 401 -> Mono.error(new AuthUnauthorizedException());
                                    case 403 -> Mono.error(new AuthForbiddenException());
                                    default -> Mono.error(new RuntimeException());
                                }
                                return null;
                                }
                            )
                    .bodyToMono(CustomerDTO.class);

            return user.block();

        } catch (Exception exception) {
            log.error(exception.getMessage());
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceCustomerUnavailableException();
            } else {
                throw exception;
            }
        }

    }

}
