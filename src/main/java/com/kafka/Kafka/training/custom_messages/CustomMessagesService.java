package com.kafka.Kafka.training.custom_messages;


import com.kafka.Kafka.training.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class CustomMessagesService {

    @Autowired
    KafkaTemplate<String, Object> customMessageKafkaTemplate;

    public void sendMessage(String name, String surname, String topic) {
        var user = UserDto.builder()
                .name(name)
                .surname(surname)
                .build();
        var kafkaRecord = Utils.createRecord(user, topic, null);
        var future = customMessageKafkaTemplate.send(kafkaRecord);
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

    @KafkaListener(topics = "custom-messages-topic", containerFactory = "customMessagesKafkaListenerContainerFactory")
    public void userListener(UserDto userDto) {
        log.info("Received user message: {}", userDto);
    }
}
