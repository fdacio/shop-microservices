package br.com.daciosoftware.shop.auth.service;

import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerCpfExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceCustomerUnavailableException;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class CustomerService {

    @Value("${customer.api.url}")
    private String customerApiURL;

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        try {

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
                                    String messageError = response.bodyToMono(String.class).block();
                                    if (messageError != null) {
                                        if (messageError.toLowerCase().contains("cpf")) {
                                            log.error("CPF already exists: {}", customerDTO.getCpf());
                                            return Mono.error(new CustomerCpfExistsException());
                                        }
                                        if (messageError.toLowerCase().contains("email")) {
                                            log.error("Email already exists: {}", customerDTO.getEmail());
                                            return Mono.error(new CustomerEmailExistsException());
                                        }
                                    }
                                }
                                return Mono.empty();
                            }
                    )
                    .bodyToMono(CustomerDTO.class);

            return customer.block();

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceCustomerUnavailableException();
            } else {
                throw exception;
            }
        }
    }

    public Optional<CustomerDTO> findByKeyAuth(String keyAuth) {

        try {

            WebClient webClient = WebClient.builder()
                    .baseUrl(customerApiURL)
                    .build();

            Mono<CustomerDTO> customer = webClient
                    .get()
                    .uri("/customer/optional/" + keyAuth + "/key-auth")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                default  -> Mono.empty();
                            }
                    )
                    .bodyToMono(CustomerDTO.class);

            return Optional.ofNullable(customer.block());

        } catch (Exception exception) {
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceCustomerUnavailableException();
            } else {
                throw exception;
            }
        }
    }

}
