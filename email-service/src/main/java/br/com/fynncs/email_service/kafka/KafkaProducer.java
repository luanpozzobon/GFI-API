package br.com.fynncs.email_service.kafka;

import br.com.fynncs.email_service.comum.ObjectMapper;
import br.com.fynncs.email_service.model.EmailType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicManager topicCreator;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaTopicManager topicCreator) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicCreator = topicCreator;
    }

    public void send(String topic, EmailType emailType) {
        createTopic(topic, 1, 1);
        try {
            kafkaTemplate.send(topic, ObjectMapper.writeValueAsString(emailType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendConfirmEmail(String key, Object object) throws JsonProcessingException {
        final String topic = "confirm-email";
        createTopic(topic, 1, 1);
        kafkaTemplate.send(topic, key, ObjectMapper.writeValueAsString(object));
    }

    private void createTopic(String topic, int partitions, int replicationFactor) {
        topicCreator.createTopic(topic, partitions, (short) replicationFactor);
    }
}

