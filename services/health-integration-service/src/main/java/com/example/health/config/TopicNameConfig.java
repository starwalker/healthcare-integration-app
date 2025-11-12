package com.example.health.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicNameConfig {
    @Value("${app.kafka.topic}")
    private String topic;

    @Bean
    public String topicName() {
        return topic;
    }
}
