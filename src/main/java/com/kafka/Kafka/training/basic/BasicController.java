package com.kafka.Kafka.training.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/basic")
@Validated
public class BasicController {

    @Autowired
    BasicService basicService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/send-message")
    public ResponseEntity<String> test(@RequestParam String message,
                                       @RequestParam @NotNull @Pattern(regexp = "^test-topic|paradigma-topic|test-topic-headers$",
                                               message = "Not valid topic. Values allowed: test-topic|paradigma-topic|test-topic-headers") String topic) {
        basicService.sendMessage(message, topic);
        return ResponseEntity.ok("¡Mensaje enviado!");
    }
}
