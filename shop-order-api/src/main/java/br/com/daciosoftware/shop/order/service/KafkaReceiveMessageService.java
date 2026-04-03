package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerCredcardRequestDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerCredcardResponseDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReceiveMessageService {

    private static final String ORDER_TOPIC_NAME = "ORDER_TOPIC";
    private static final String ORDER_TOPIC_EVENT_NAME = "ORDER_TOPIC_EVENT";

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private final OrderService orderService;
    private final BrokerCredcardService brokerCredcardService;

    @KafkaListener(topics = ORDER_TOPIC_NAME, groupId = "order-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenerOrderTopic(OrderDTO order) {

        log.info("Order Kafka receive: {}", order.getId());

        CustomerDTO customer = order.getCustomer();
        CredcardDTO credcard = order.getCredcardPrincipal();
        Float valorTotalOrder = order.getTotal();


        BrokerCredcardRequestDTO request = new BrokerCredcardRequestDTO();
        request.setOrderId(order.getId());
        request.setCpf(customer.getCpf());
        request.setNumberCard(credcard.getNumberCard());
        request.setCvv(credcard.getCvv());
        request.setValue(valorTotalOrder);

        log.info("Broker credcard request: {}", request);

        Mono<BrokerCredcardResponseDTO> responseMono = brokerCredcardService.processPayment(request);

        if (responseMono.blockOptional().isPresent()) {
            BrokerCredcardResponseDTO response = responseMono.block();
            log.info("Resposta do broker de cartão: {}", response);

            if (response != null && response.getAuthorized()) {
                orderService.updateStatus(order, OrderStatus.APPROVED);
                log.info("Order processada com sucesso");
            } else {
                orderService.updateStatus(order, OrderStatus.REJECTED);
                log.info("Order rejeitada pelo broker de cartão");
            }
        } else {
            orderService.updateStatus(order, OrderStatus.REJECTED);
            log.info("Order rejeitada: resposta do broker de cartão não recebida");
        }

    }


}
