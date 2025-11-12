package com.example.health.kafka;

import com.example.health.model.PatientEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final KafkaTemplate<String, PatientEvent> kafkaTemplate;
    private final String topic;

    public ProducerService(KafkaTemplate<String, PatientEvent> kafkaTemplate,
                           @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(PatientEvent event) {
        String key = event.getPatientId();
        kafkaTemplate.send(topic, key, event);
    }
}
