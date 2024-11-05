package org.sdi.usermanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.usermanager.service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
