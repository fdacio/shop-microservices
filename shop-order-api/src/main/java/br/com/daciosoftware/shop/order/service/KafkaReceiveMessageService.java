package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.customer.CredcardDTO;
import br.com.daciosoftware.shop.models.dto.customer.CustomerDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentRequestDTO;
import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentResponseDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderPaymentDTO;
import br.com.daciosoftware.shop.models.enums.OrderStatus;
import br.com.daciosoftware.shop.order.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
    public void listenerCreateOrder(OrderDTO order) {
        log.info("Order Kafka order create listener: {}", order.getId());
        CustomerDTO customer = order.getCustomer();
        CredcardDTO credcard = order.getCredcardPrincipal();
        Float valorTotalOrder = order.getTotal();
        BrokerPaymentRequestDTO request = new BrokerPaymentRequestDTO();
        request.setOrderId(order.getId());
        request.setCredcardId(credcard.getId());
        request.setCpf(customer.getCpf());
        request.setNumberCard(credcard.getNumberCard());
        request.setCvv(credcard.getCvv());
        request.setValue(valorTotalOrder);
        brokerCredcardService.processPayment(request);
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
