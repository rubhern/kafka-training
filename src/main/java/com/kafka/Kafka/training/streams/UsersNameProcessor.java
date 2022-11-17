package com.kafka.Kafka.training.streams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsersNameProcessor {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
                .stream("user-topic", Consumed.with(STRING_SERDE, STRING_SERDE))
                .peek((key, value) -> log.info("Received message: {}", value))
                .map((key, value) -> KeyValue.pair(key, value))
                .peek((key, value) -> log.info("Mapped message: {}", value));
        messageStream.to("output-user-complete-name");
    }
}
