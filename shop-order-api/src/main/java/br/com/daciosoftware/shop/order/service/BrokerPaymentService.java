package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentRequestDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerPaymentService {

    @Value("${broker.cardcard.url}")
    private String brokerCredcardApiURL;
    private final KafkaClientService kafkaClientService;

    public void processPayment(BrokerPaymentRequestDTO request) {

        BrokerPaymentResponseDTO response = new BrokerPaymentResponseDTO();
        response.setOrderId(request.getOrderId());
        response.setCredcardId(request.getCredcardId());

        WebClient webClient = WebClient.builder()
                .baseUrl(brokerCredcardApiURL)
                .build();

        webClient
                .post()
                .uri("/authorization")
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.isError() || status.is2xxSuccessful(),
                        clientResponse -> {
                            log.info("Broke Payment response status: {}", clientResponse.statusCode().value());
                            return clientResponse
                                    .bodyToMono(BrokerPaymentResponseDTO.class)
                                    .flatMap(brokerPaymentResponseDTO -> {
                                        response.setAuthorized(brokerPaymentResponseDTO.getAuthorized());
                                        response.setCodeResponse(clientResponse.statusCode().value());
                                        response.setMessage(brokerPaymentResponseDTO.getMessage());
                                        kafkaClientService.sendResponseBrokerPayment(response);
                                        return Mono.empty();
                                    });

                        }
                )
                .bodyToMono(Void.class)
                .doOnError(error -> {
                    response.setAuthorized(false);
                    response.setCodeResponse(error.hashCode());
                    response.setMessage("Erro broker de pagamento. Tente mais tarde ");
                    kafkaClientService.sendResponseBrokerPayment(response);
                })
                .subscribe();

    }
}
