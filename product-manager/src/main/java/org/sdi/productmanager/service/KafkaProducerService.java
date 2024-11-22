package org.sdi.productmanager.service;

import org.springframework.stereotype.Component;

@Component
public interface KafkaProducerService {
    void sendMessage(String topic, String message);
}
