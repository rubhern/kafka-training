package com.kafka.Kafka.training.streams.testContainer;

import com.kafka.Kafka.training.KafkaTrainingApplication;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;
import static org.testcontainers.shaded.org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@Import(com.kafka.Kafka.training.streams.testContainer.KafkaTestContainersLiveTest.KafkaTestContainersConfiguration.class)
@SpringBootTest(classes = KafkaTrainingApplication.class)
@DirtiesContext
public class KafkaTestContainersLiveTest {

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Autowired
    public KafkaTemplate<String, String> template;

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    @Value("${test.topic}")
    private String topic;

    @Before
    public void setup() {
        consumer.resetLatch();
    }

    @Test
    public void givenKafkaDockerContainer_whenSendingWithDefaultTemplate_thenMessageReceived() throws Exception {
        String data = "Sending with default template";

        template.send(topic, data);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(consumer.getPayload(), containsString(data));
    }

    @Test
    public void givenKafkaDockerContainer_whenSendingWithSimpleProducer_thenMessageReceived() throws Exception {
        String data = "Sending with our own simple KafkaProducer";

        producer.send(topic, data);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(consumer.getPayload(), containsString(data));
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

    }
}
