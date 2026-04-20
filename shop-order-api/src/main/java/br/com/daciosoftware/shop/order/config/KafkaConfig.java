package br.com.daciosoftware.shop.order.config;

import br.com.daciosoftware.shop.models.dto.order.BrokerPaymentResponseDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddressServers;

    public static final String ORDER_CREATE_TOPIC = "ORDER_CREATE_TOPIC";
    public static final String BROKER_PAYMENT_RESPONSE_TOPIC = "BROKER_PAYMENT_RESPONSE_TOPIC";
    public static final String REPROCESS_PAYMENT_TOPIC = "REPROCESS_PAYMENT_TOPIC";

    public ProducerFactory<String, OrderDTO> producerFactoryOrderCreate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddressServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "order-api");
        return new DefaultKafkaProducerFactory<>(props);
    }

    public ProducerFactory<String, BrokerPaymentResponseDTO> producerFactoryBrokerPaymentResponse() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddressServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "order-api");
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, OrderDTO> kafkaTemplateCreateOrder() {
        return new KafkaTemplate<>(producerFactoryOrderCreate());
    }

    @Bean
    public KafkaTemplate<String, BrokerPaymentResponseDTO> kafkaTemplateBrokerPaymentResponse() {
        return new KafkaTemplate<>(producerFactoryBrokerPaymentResponse());
    }

    public ConsumerFactory<String, OrderDTO> consumerFactoryOrderCreate() {
        // JSON deserializer que conhece a classe alvo
        JsonDeserializer<OrderDTO> deserializer = new JsonDeserializer<>(OrderDTO.class);
        // Permitir desserializar classes do pacote do projeto (evita erro de "trusted packages")
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddressServers);
        // garantir group id e comportamento de offset para consistência
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    public ConsumerFactory<String, OrderDTO> consumerFactoryProcessPayment() {
        // JSON deserializer que conhece a classe alvo
        JsonDeserializer<OrderDTO> deserializer = new JsonDeserializer<>(OrderDTO.class);
        // Permitir desserializar classes do pacote do projeto (evita erro de "trusted packages")
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddressServers);
        // garantir group id e comportamento de offset para consistência
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    public ConsumerFactory<String, BrokerPaymentResponseDTO> consumerFactoryBrokerPaymentResponse() {
        // JSON deserializer que conhece a classe alvo
        JsonDeserializer<BrokerPaymentResponseDTO> deserializer = new JsonDeserializer<>(BrokerPaymentResponseDTO.class);
        // Permitir desserializar classes do pacote do projeto (evita erro de "trusted packages")
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddressServers);
        // garantir group id e comportamento de offset para consistência
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDTO> kafkaListenerContainerFactoryOrderCreate() {
        ConcurrentKafkaListenerContainerFactory<String, OrderDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOrderCreate());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BrokerPaymentResponseDTO> kafkaListenerContainerFactoryBrokerPaymentResponse() {
        ConcurrentKafkaListenerContainerFactory<String, BrokerPaymentResponseDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryBrokerPaymentResponse());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDTO> kafkaListenerContainerFactoryProcessPayment() {
        ConcurrentKafkaListenerContainerFactory<String, OrderDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryProcessPayment());
        return factory;
    }

    public NewTopic createOrderTopic() {
        return TopicBuilder.name(ORDER_CREATE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic brokerPaymentResponseTopic() {
        return TopicBuilder.name(BROKER_PAYMENT_RESPONSE_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
