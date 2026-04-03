package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.order.BrokerCredcardRequestDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerCredcardResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class BrokerCredcardService {

    @Value("${broker.cardcard.url}")
    private String brokerCredcardApiURL;

    public Mono<BrokerCredcardResponseDTO> processPayment(BrokerCredcardRequestDTO request) {

        WebClient webClient = WebClient.builder()
                .baseUrl(brokerCredcardApiURL)
                .build();

        return webClient
                .post()
                .uri("/authorization")
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse ->
                                clientResponse.bodyToMono(BrokerCredcardResponseDTO.class)
                                        .flatMap(errorBody -> {
                                            log.error("Erro ao processar pagamento: {}", errorBody.getMessage());
                                            return Mono.error(new RuntimeException(errorBody.getMessage()));
                                        })
                )
                .bodyToMono(BrokerCredcardResponseDTO.class);
    }
}
