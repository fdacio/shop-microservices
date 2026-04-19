package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.exceptions.exceptions.customer.CredcardPrincipalNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerInvalidKeyException;
import br.com.daciosoftware.shop.exceptions.exceptions.customer.CustomerNotFoundException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthForbiddenException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.AuthUnauthorizedException;
import br.com.daciosoftware.shop.exceptions.exceptions.gateway.MicroserviceCustomerUnavailableException;
import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
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

    public CustomerDTO getCustomerAuthenticated(String token) {

        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(customerApiURL)
                    .build();

            Mono<CustomerDTO> customer = webClient
                    .post()
                    .uri("/customer/authenticated")
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 409 -> Mono.error(new CustomerInvalidKeyException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                case 404 -> Mono.error(new CustomerNotFoundException());
                                default -> Mono.error(new RuntimeException());
                            }
                    )
                    .bodyToMono(CustomerDTO.class);

            return customer.block();

        } catch (Exception exception) {
            log.error(exception.getMessage());
            if (exception instanceof WebClientRequestException) {
                throw new MicroserviceCustomerUnavailableException();
            } else {
                throw exception;
            }
        }

    }

    public CredcardDTO getCredcardPrincipalByToken(String token) {

        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(customerApiURL)
                    .build();

            Mono<CredcardDTO> credcard = webClient
                    .post()
                    .uri("/customer/my-principal-credcard")
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> switch (response.statusCode().value()) {
                                case 409 -> Mono.error(new CustomerInvalidKeyException());
                                case 401 -> Mono.error(new AuthUnauthorizedException());
                                case 403 -> Mono.error(new AuthForbiddenException());
                                case 404 -> Mono.error(new CredcardPrincipalNotFoundException());
                                default -> Mono.error(new RuntimeException());
                            }
                    )
                    .bodyToMono(CredcardDTO.class);

            return credcard.block();

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
