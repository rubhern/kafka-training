package com.kafka.Kafka.training.basic;


import com.kafka.Kafka.training.config.KafkaApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class BasicService {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    KafkaApplicationProperties properties;

    public void sendMessage(String message, String topic) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "test-topic", groupId = "test-training")
    public void listenTest(String message) {
        log.info("Received Message in group {}: {}", properties.getGroupId(), message);
    }

    @KafkaListener(topics = "another-topic", groupId = "test-training")
    public void listenAnother(String message) {
        log.info("Received Message in group {}: {}", properties.getGroupId(), message);
    }

    @KafkaListener(topics = "test-topic")
    public void listenWithHeaders(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("Received Message: " + message + " from partition: " + partition);
    }
}
