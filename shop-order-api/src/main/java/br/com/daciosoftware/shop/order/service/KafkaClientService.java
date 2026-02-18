package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaClientService {

    private final KafkaTemplate<String, OrderDTO> kafkaTemplate;
    private static final String ORDER_TOPIC_NAME = "ORDER_TOPIC";

    public void sendMessage(OrderDTO msg) {
        kafkaTemplate.send(ORDER_TOPIC_NAME, msg);
    }
}
