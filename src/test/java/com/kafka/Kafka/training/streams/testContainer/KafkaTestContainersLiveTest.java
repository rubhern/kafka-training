package com.kafka.Kafka.training.streams.testContainer;

import com.kafka.Kafka.training.KafkaTrainingApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.containsString;

@Testcontainers
@Import(KafkaTestContainersLiveTest.KafkaTestContainersConfiguration.class)
@SpringBootTest(classes = KafkaTrainingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class KafkaTestContainersLiveTest {

    @Container
    private static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Autowired
    public KafkaTemplate<String, String> testContainerKafkaTemplate;

    private CountDownLatch latch = new CountDownLatch(1);

    private String payload;

    @KafkaListener(topics = "testcontainers-topic", containerFactory = "consumerTestContainerFactory")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        log.info("received payload='{}'", consumerRecord.toString());

        payload = consumerRecord.toString();
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    public String getPayload() {
        return payload;
    }

    @Before
    public void setup() {
        resetLatch();
    }

    @Test
    public void givenKafkaDockerContainer_whenSendingWithDefaultTemplate_thenMessageReceived() throws Exception {
        String data = "Sending with default template";

        testContainerKafkaTemplate.send("testcontainers-topic", data);

        boolean messageConsumed = getLatch().await(50, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(getPayload(), containsString(data));
    }

    @Test
    public void givenKafkaDockerContainer_whenSendingWithSimpleProducer_thenMessageReceived() throws Exception {
        String data = "Sending with our own simple KafkaProducer";

        testContainerKafkaTemplate.send("testcontainers-topic", data);

        boolean messageConsumed = getLatch().await(50, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(getPayload(), containsString(data));
    }

    @TestConfiguration
    static class KafkaTestContainersConfiguration {

        @Bean
        ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerTestContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerTestContainerFactory());
            return factory;
        }

        @Bean
        public ConsumerFactory<Integer, String> consumerTestContainerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
        }

        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "baeldung");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            return props;
        }

        @Bean
        public ProducerFactory<String, String> producerTestContainerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, String> testContainerKafkaTemplate() {
            return new KafkaTemplate<>(producerTestContainerFactory());
        }

        @Bean
        public KafkaAdmin testContainerKafkaAdmin() {
            Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            return new KafkaAdmin(configs);
        }

    }
}
