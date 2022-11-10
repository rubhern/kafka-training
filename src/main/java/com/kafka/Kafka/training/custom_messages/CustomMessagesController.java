package com.kafka.Kafka.training.custom_messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custom-messages")
@Validated
public class CustomMessagesController {

    @Autowired
    CustomMessagesService customMessagesService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/send-message")
    public ResponseEntity<String> test(@RequestParam String name,
                                       @RequestParam String surname) {
        customMessagesService.sendMessage(name, surname, "custom-messages-topic");
        return ResponseEntity.ok("¡Mensaje enviado!");
    }
}
