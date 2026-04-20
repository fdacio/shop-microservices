package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentResponseDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderPaymentDTO;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import br.com.daciosoftware.shop.order.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaReceiveMessageService {

    private final OrderService orderService;
    private final BrokerPaymentService brokerCredcardService;
    private final OrderPaymentService orderPaymentService;

    @KafkaListener(topics = KafkaConfig.ORDER_CREATE_TOPIC, groupId = "order-group", containerFactory = "kafkaListenerContainerFactoryOrderCreate")
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 5000, multiplier = 2),
            kafkaTemplate = "kafkaTemplateCreateOrder",
            dltTopicSuffix = ".DLT"
    )
    public void listenerCreateOrder(OrderDTO order) {
        log.info("Order Kafka order create listener: {}", order.getId());
        brokerCredcardService.processPayment(order);
    }

    @KafkaListener(topics = KafkaConfig.BROKER_PAYMENT_RESPONSE_TOPIC, groupId = "order-group", containerFactory = "kafkaListenerContainerFactoryBrokerPaymentResponse")
    public void listenerBrokerPayment(BrokerPaymentResponseDTO response) {
        log.info("Broker payment response kafka listener: {}", response);
        OrderStatus status = response.getAuthorized() ? OrderStatus.APPROVED : OrderStatus.REJECTED;
        orderService.updateStatus(response.getOrderId(), status);

        OrderPaymentDTO orderPaymentDTO = new OrderPaymentDTO();
        orderPaymentDTO.setOrderId(response.getOrderId());
        orderPaymentDTO.setCredcardId(response.getCredcardId());
        orderPaymentDTO.setDatePayment(LocalDateTime.now());
        orderPaymentDTO.setStatus(status);
        orderPaymentDTO.setMessage(response.getMessage());
        orderPaymentService.save(orderPaymentDTO);
    }


}
