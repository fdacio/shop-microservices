package br.com.daciosoftware.shop.auth.service;


import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerCpfExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.ServiceCustomerUnavailableException;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CustomerService {

    @Value("${customer.api.url}")
    private String customerApiURL;

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        WebClient webClient = WebClient.builder()
                .baseUrl(customerApiURL)
                .build();

        Mono<CustomerDTO> customer = webClient
                .post()
                .uri("/customer")
                .bodyValue(customerDTO)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> {
                            if (response.statusCode().is4xxClientError()) {
                                ErrorDTO errorDTO = response.bodyToMono(ErrorDTO.class).block();
                                if (errorDTO != null) {
                                    if (errorDTO.getMessage().toLowerCase().contains("cpf")) {
                                        return Mono.error(new CustomerCpfExistsException());
                                    }
                                    if (errorDTO.getMessage().toLowerCase().contains("email")) {
                                        return Mono.error(new CustomerEmailExistsException());
                                    }
                                    return Mono.error(new ServiceCustomerUnavailableException());
                                }
                            }
                            return Mono.empty();
                        }
                )
                .bodyToMono(CustomerDTO.class);

        return customer.block();

    }

    public Optional<CustomerDTO> findByKeyAuth(String keyAuth) {

        try {

            WebClient webClient = WebClient.builder()
                    .baseUrl(customerApiURL)
                    .build();

            Mono<CustomerDTO> customer = webClient
                    .get()
                    .uri("/customer/" + keyAuth + "/key-auth")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 409 -> Mono.error(new CustomerInvalidKeyException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default -> Mono.error(new RuntimeException());
                            }
                    )
                    .bodyToMono(CustomerDTO.class);

            return Optional.ofNullable(customer.block());

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new ServiceCustomerUnavailableException();
            } else {
                throw exception;
            }
        }
    }

}
