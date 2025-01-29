package br.com.fynncs.email_service.kafka;

import br.com.fynncs.email_service.comum.ObjectMapper;
import br.com.fynncs.email_service.config.KafkaConfig;
import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Service
public class KafkaConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "email", groupId = "email-send")
    public void consumer(String message) {
        try {
            EmailType emailType = ObjectMapper.readValue(message, EmailType.class);
            emailService.sendEmail(emailType.getTo(), emailType.getSubject(), emailType.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void consumerTransaction(String id) {
        Map<String, Object> config = KafkaConfig.consumerConfig(true);
        try(org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer =
                new org.apache.kafka.clients.consumer.KafkaConsumer<>(config)) {
            consumer.subscribe(Collections.singletonList("confirm-email"));
            try{
                int cont = 0;
                while (cont < 100) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    for (ConsumerRecord<String, String> record : records) {
                        if(id.equalsIgnoreCase(record.key())) {
                            System.out.println("Mensagem: " + record.value());
                            consumer.commitSync(Collections.singletonMap(
                                    new TopicPartition(record.topic(), record.partition()),
                                    new OffsetAndMetadata(record.offset() + 1)
                            ));
                            return;
                        }
                    }
                    cont++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                consumer.close();
            }
        }

    }
}