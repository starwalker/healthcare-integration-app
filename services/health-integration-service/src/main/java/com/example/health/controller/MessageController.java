package com.example.health.controller;

import com.example.health.kafka.ProducerService;
import com.example.health.model.PatientEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class MessageController {

    private final ProducerService producerService;

    public MessageController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/patient")
    public ResponseEntity<?> publish(@RequestBody PatientEvent event) {
        producerService.publish(event);
        return ResponseEntity.accepted().build();
    }
}
