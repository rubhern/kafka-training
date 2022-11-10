package com.kafka.Kafka.training.schema_registry.config;

import com.kafka.Kafka.training.shared.config.KafkaApplicationProperties;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

@Configuration
public class SchemaRegisterKafkaProducerConfig {
    @Autowired
    private KafkaApplicationProperties properties;

    @Bean("schemaRegisterProducerFactory")
    public ProducerFactory<String, Object> schemaRegisterProducerFactory() {
        var configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServer());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean("schemaRegisterKafkaTemplate")
    public KafkaTemplate<String, Object> schemaRegisterKafkaTemplate() {
        return new KafkaTemplate<>(schemaRegisterProducerFactory());
    }
}
