package com.kafka.Kafka.training.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaApplicationProperties {

    @NotNull
    private String server;

    @NotNull
    private String groupId;

}
