package com.kafka.Kafka.training;

import org.apache.kafka.clients.producer.ProducerRecord;

public class Utils {

    public static ProducerRecord<String, String> createRecord(final String message, final String topic, final String key) {
        return new ProducerRecord<>(topic, key, message);

    }

    public static ProducerRecord<String, Object> createRecord(final Object message, final String topic, final String key) {
        return new ProducerRecord<>(topic, key, message);

    }
}
