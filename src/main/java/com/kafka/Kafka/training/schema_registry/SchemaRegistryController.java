package com.kafka.Kafka.training.schema_registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schema-registry")
@Validated
public class SchemaRegistryController {

    @Autowired
    SchemaRegistryService schemaRegistryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/send-message")
    public ResponseEntity<String> test(@RequestParam String name,
                                       @RequestParam String surname) {
        schemaRegistryService.sendMessage(name, surname, "user-topic");
        return ResponseEntity.ok("¡Mensaje enviado!");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/send-message-ko")
    public ResponseEntity<String> testKo(@RequestParam String name) {
        schemaRegistryService.sendBadMessage(name, "user-topic");
        return ResponseEntity.ok("¡Mensaje enviado!");
    }
}
