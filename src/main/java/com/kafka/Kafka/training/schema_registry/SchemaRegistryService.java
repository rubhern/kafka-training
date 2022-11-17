package com.kafka.Kafka.training.schema_registry;


import com.kafka.Kafka.training.Utils;
import io.confluent.kafka.schemaregistry.ParsedSchema.UserSchemaRegistryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class SchemaRegistryService {

    @Autowired
    KafkaTemplate<String, Object> schemaRegisterKafkaTemplate;

    public void sendMessage(String name, String surname, String topic) {
        var user = new UserSchemaRegistryDto();
        user.setName(name);
        user.setSurname(surname);
        var kafkaRecord = Utils.createRecord(user, topic, null);
        var future = schemaRegisterKafkaTemplate.send(kafkaRecord);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message=[" + user +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=["
                        + user + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendBadMessage(String name, String topic) {
        var user = new UserSchemaRegistryDto();
        user.setName(name);
        var kafkaRecord = Utils.createRecord(user, topic, null);
        var future = schemaRegisterKafkaTemplate.send(kafkaRecord);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message=[" + user +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=["
                        + user + "] due to : " + ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "user-topic", containerFactory = "schemaRegistryKafkaListenerContainerFactory")
    public void userListener(UserSchemaRegistryDto userDto) {
        log.info("Received user message: {}", userDto);
    }
}
