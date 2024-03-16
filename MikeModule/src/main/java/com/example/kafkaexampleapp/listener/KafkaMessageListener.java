package com.example.kafkaexampleapp.listener;

import com.example.kafkaexampleapp.model.KafkaMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageListener {
    private static Long id = 1L;
    private final KafkaTemplate<String, KafkaMessage> template;
    @Value("${app.kafka.myKafkaMessageTopic}")
    private String myTopic;

    @SneakyThrows
    @KafkaListener(
            topics = "${app.kafka.kafkaMessageTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory"
    )
    public void listen(@Payload KafkaMessage message,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(value = KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(value = KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        log.info("Received message: {}", message);
        log.info("Received key: {}", key);
        log.info("Received topic: {}", topic);
        log.info("Received partition: {}", partition);
        log.info("Received timestamp: {}", timestamp);

        Thread.currentThread().sleep(1000);

        template.send(myTopic, new KafkaMessage(id, "Hi Henry! Hold the cookie: " + UUID.randomUUID()));
        id++;
    }

    @PostConstruct
    public void start() {
        template.send(myTopic, new KafkaMessage(id, "Hi Henry! Hold the cookie: " + UUID.randomUUID()));
        log.info("First message send");
    }
}
