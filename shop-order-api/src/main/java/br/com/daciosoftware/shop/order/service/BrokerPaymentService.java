package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.customer.CredcardShotDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentRequestDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentResponseDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
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

    public void processPayment(OrderDTO order) {

        BrokerPaymentRequestDTO requestDTO = getBrokerPaymentRequestDTO(order);

        WebClient.builder()
                .baseUrl(brokerCredcardApiURL)
                .build()
                .post()
                .uri("/authorization")
                .bodyValue(requestDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.warn("Erro de validação do broker para ordem {}: status {}", order.getId(), clientResponse.statusCode().value());
                    return clientResponse
                            .bodyToMono(BrokerPaymentResponseDTO.class)
                            .flatMap(errorResponse -> {
                                BrokerPaymentResponseDTO response = getBrokerPaymentResponseDTO(order, errorResponse);
                                kafkaClientService.sendResponseBrokerPayment(response);
                                return Mono.empty();
                            });
                })
                .bodyToMono(BrokerPaymentResponseDTO.class)
                .flatMap(brokerResponse -> {
                    log.info("Processando o pagamento da Ordem: {}", order.getId());
                    BrokerPaymentResponseDTO response = getBrokerPaymentResponseDTO(order, brokerResponse);
                    kafkaClientService.sendResponseBrokerPayment(response);
                    return Mono.empty();
                })
                .onErrorResume(error -> {
                    log.error("Erro no processamento do pagamento para ordem: {} - {}", order.getId(), error.getMessage());
                    BrokerPaymentResponseDTO errorResponse = createErrorResponse(order, error);
                    kafkaClientService.sendResponseBrokerPayment(errorResponse);
                    // Lança a exception após enviar para Kafka, para que o Kafka faça retry
                    return Mono.error(error);
                })
                .block(); // Bloqueia para lançar exception se houver erro

    }

    private BrokerPaymentRequestDTO getBrokerPaymentRequestDTO(OrderDTO order) {
        CustomerDTO customer = order.getCustomer();
        CredcardShotDTO credcardPrincipal = order.getCredcardPrincipal();
        Long orderId = order.getId();
        Long credcardId = credcardPrincipal.getId();
        String cpf = customer.getCpf();
        String numberCard = credcardPrincipal.getNumberCard();
        Integer cvv = credcardPrincipal.getCvv();
        Float valorTotalOrder = order.getTotal();

        BrokerPaymentRequestDTO request = new BrokerPaymentRequestDTO();
        request.setOrderId(orderId);
        request.setCredcardId(credcardId);
        request.setCpf(cpf);
        request.setNumberCard(numberCard);
        request.setCvv(cvv);
        request.setValue(valorTotalOrder);
        return request;
    }

    private BrokerPaymentResponseDTO getBrokerPaymentResponseDTO(OrderDTO orderDTO, BrokerPaymentResponseDTO brokerPaymentResponseDTO) {
        BrokerPaymentResponseDTO response = new BrokerPaymentResponseDTO();
        response.setOrderId(orderDTO.getId());
        response.setCredcardId(orderDTO.getCredcardPrincipal().getId());
        response.setAuthorized(brokerPaymentResponseDTO.getAuthorized());
        response.setCodeResponse(brokerPaymentResponseDTO.getCodeResponse());
        response.setMessage(brokerPaymentResponseDTO.getMessage());
        return response;
    }

    private BrokerPaymentResponseDTO createErrorResponse(OrderDTO order, Throwable error) {
        BrokerPaymentResponseDTO response = new BrokerPaymentResponseDTO();
        response.setOrderId(order.getId());
        response.setCredcardId(order.getCredcardPrincipal().getId());
        response.setAuthorized(false);
        response.setCodeResponse(error.hashCode());
        response.setMessage("Erro ao processar o pagamento. Tente novamente mais tarde.");
        return response;
    }
}
