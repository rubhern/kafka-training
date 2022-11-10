package com.kafka.Kafka.training.custom_messages.config;

import com.kafka.Kafka.training.shared.config.KafkaApplicationProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

@Configuration
public class CustomMessageKafkaProducerConfig {
    @Autowired
    private KafkaApplicationProperties properties;

    @Bean("customMessageProducerFactory")
    public ProducerFactory<String, Object> customMessageProducerFactory() {
        var configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServer());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean("customMessageKafkaTemplate")
    public KafkaTemplate<String, Object> customMessageKafkaTemplate() {
        return new KafkaTemplate<>(customMessageProducerFactory());
    }
}
