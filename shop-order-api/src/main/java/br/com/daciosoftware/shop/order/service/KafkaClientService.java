package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentResponseDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.order.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaClientService {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplateOrder;
    private final KafkaTemplate<String, BrokerPaymentResponseDTO> kafkaTemplateBrokerPaymentResponse;

    public void sendOrder(OrderDTO orderDTO) {
        kafkaTemplateOrder.send(KafkaConfig.ORDER_CREATE_TOPIC, orderDTO);
    }

    public void sendResponseBrokerPayment(BrokerPaymentResponseDTO response) {
        kafkaTemplateBrokerPaymentResponse.send(KafkaConfig.BROKER_PAYMENT_RESPONSE_TOPIC, response);
    }

    public void sendProcessPayment(OrderDTO orderDTO) {
        kafkaTemplateOrder.send(KafkaConfig.REPROCESS_PAYMENT_TOPIC, orderDTO);
    }
}
