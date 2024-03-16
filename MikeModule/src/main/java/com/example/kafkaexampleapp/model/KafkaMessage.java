package com.example.kafkaexampleapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaMessage {
    private Long id;
    private String message;
}
