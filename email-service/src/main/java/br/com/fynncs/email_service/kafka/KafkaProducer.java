package br.com.fynncs.email_service.kafka;

import br.com.fynncs.email_service.model.EmailType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        topicCreator.createTopic(topic, 1, (short) 1);
        try {
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send(topic, mapper.writeValueAsString(emailType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

