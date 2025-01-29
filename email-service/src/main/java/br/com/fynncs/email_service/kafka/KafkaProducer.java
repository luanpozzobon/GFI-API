package br.com.fynncs.email_service.kafka;

import br.com.fynncs.email_service.comum.ObjectMapper;
import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.record.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicCreator topicCreator;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaTopicCreator topicCreator) {
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

    public void sendConfirmEmail(String key, User user) {
        final String topic = "confirm-email";
        createTopic(topic, 1, 1);
        kafkaTemplate.executeInTransaction(operations -> {
            try {
                operations.send(topic, key, ObjectMapper.writeValueAsString(user));
                return true;
            } catch (JsonProcessingException e) {
                return false;
            }
        });
    }

    private void createTopic(String topic, int partitions, int replicationFactor){
        topicCreator.createTopic(topic, partitions, (short) replicationFactor);
    }
}

