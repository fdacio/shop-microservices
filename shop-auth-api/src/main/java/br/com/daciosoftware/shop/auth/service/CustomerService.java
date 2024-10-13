package br.com.daciosoftware.shop.auth.service;


import br.com.daciosoftware.shop.exceptions.dto.ErrorDTO;
import br.com.daciosoftware.shop.exceptions.exceptions.CustomerCpfExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.CustomerEmailExistsException;
import br.com.daciosoftware.shop.exceptions.exceptions.ShopGenericException;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Value("${customer.api.url}")
    private String customerApiURL;

    @Transactional
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
                            ErrorDTO errorDTO = response.bodyToMono(ErrorDTO.class).block();
                            if (errorDTO != null) {
                                if (errorDTO.getMessage().toLowerCase().contains("cpf")) {
                                    return Mono.error(new CustomerCpfExistsException());
                                }
                                if (errorDTO.getMessage().toLowerCase().contains("email")) {
                                    return Mono.error(new CustomerEmailExistsException());
                                }
                                return Mono.error(new ShopGenericException(errorDTO.getMessage()));

                            } else {
                                return Mono.error(new ShopGenericException("Error no microsserviço customer"));
                            }
                        }
                )
                .bodyToMono(CustomerDTO.class);

        return customer.block();

    }
}
