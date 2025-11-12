package com.example.health.kafka;

import com.example.health.model.PatientEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "#{@topicName}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload PatientEvent event) {
        // TODO: add business logic, idempotency check, persistence
        log.info("Consumed event patientId={} type={} ts={}", event.getPatientId(), event.getEventType(), event.getTimestamp());
    }
}
