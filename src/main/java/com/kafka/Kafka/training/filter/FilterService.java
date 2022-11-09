package com.kafka.Kafka.training.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FilterService {

    @KafkaListener(topics = "paradigma-topic", containerFactory = "filterKafkaListenerContainerFactory", groupId = "test-training")
    public void listenWithFilter(String message) {
        log.info("Received Message in filtered listener: " + message);
    }


}
