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
import java.util.*;

@Service
public class KafkaConsumer {

    @Autowired
    private EmailService emailService;

    public static void consumerTransaction(String id, KafkaProducer kafkaProducer, Class<?> clazz) throws Exception {
        Map<String, Map<Integer, List<ConsumerRecord<String, String>>>> map = new HashMap<>();
        boolean breakLoop = false;
        try (org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer =
                     new org.apache.kafka.clients.consumer.KafkaConsumer<>(KafkaConfig.consumerConfig(true))) {
            consumer.subscribe(Collections.singletonList("confirm-email"));
            for (int cont = 0; cont < 20; cont++) {
                if (breakLoop) {
                    break;
                }
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    if (id.equalsIgnoreCase(record.key())) {
                        System.out.println("Mensagem: " + record.value());
                        consumer.commitSync(Collections.singletonMap(
                                new TopicPartition(record.topic(), record.partition()),
                                new OffsetAndMetadata(record.offset() + 1)
                        ));
                        breakLoop = true;
                        continue;
                    }
                    assemblyMap(map, record);
                }
            }
        }
        ResendKafkaConsumer.deleteAndResend(map, clazz, kafkaProducer);
        if (!breakLoop) {
            throw new Exception("Erro ao confirmar Email!");
        }
    }

    private static void assemblyMap(Map<String, Map<Integer, List<ConsumerRecord<String, String>>>> map, ConsumerRecord<String, String> record) {
        Map<Integer, List<ConsumerRecord<String, String>>> partitionMap = map.getOrDefault(record.topic(), new HashMap<>());
        List<ConsumerRecord<String, String>> list = partitionMap.getOrDefault(record.partition(), new ArrayList<>());
        list.add(record);
        partitionMap.put(record.partition(), list);
        map.put(record.topic(), partitionMap);
    }

    @KafkaListener(topics = "email", groupId = "email-send")
    public void consumer(String message) {
        try {
            EmailType emailType = ObjectMapper.readValue(message, EmailType.class);
            emailService.sendEmail(emailType.getTo(), emailType.getSubject(), emailType.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}