package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReceiveMessageService {

    private static final String ORDER_TOPIC_NAME = "ORDER_TOPIC";
    private static final String ORDER_TOPIC_EVENT_NAME = "ORDER_TOPIC_EVENT";

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private final OrderService orderService;

    @KafkaListener(topics = ORDER_TOPIC_NAME, groupId = "order-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenerOrderTopic(OrderDTO order) {

        CustomerDTO customer = order.getCustomer();
        CredcardDTO credcard = customer.getCredcardPrincipal();

        Float valorTotalOrder = order.getTotal();

        try {
            log.info("Order Kafka receive: {}", order);
            if (valorTotalOrder < 1000) {
                orderService.updateStatus(order, OrderStatus.APPROVED);
                log.info("Order processada com sucesso");
            } else {
                orderService.updateStatus(order, OrderStatus.REJECTED);
                log.info("Order processada com erro");
            }
            kafkaTemplate.send(ORDER_TOPIC_EVENT_NAME, order);
        } catch (Exception e) {
            log.error("Erro ao processar Order Kafka: {}", order, e);
        }
    }



}
