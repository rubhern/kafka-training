package com.kafka.Kafka.training.custom_messages.config;

import com.kafka.Kafka.training.custom_messages.UserDto;
import com.kafka.Kafka.training.shared.config.KafkaApplicationProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;

@EnableKafka
@Configuration
public class CustomMessagesKafkaConsumerConfig {

    @Autowired
    private KafkaApplicationProperties properties;

    @Bean("customMessagesConsumerFactory")
    public ConsumerFactory<String, UserDto> customMessagesConsumerFactory() {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getServer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(UserDto.class));
    }

    @Bean("customMessagesKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, UserDto> customMessagesKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserDto>();
        factory.setConsumerFactory(customMessagesConsumerFactory());
        return factory;
    }
}
