package br.com.fynncs.email_service.kafka;

import br.com.fynncs.email_service.model.EmailType;
import br.com.fynncs.email_service.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "email", groupId = "email-send")
    public void consumer(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            EmailType emailType = mapper.readValue(message, EmailType.class);
            emailService.sendEmail(emailType.getTo(), emailType.getSubject(), emailType.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "transaction-id", groupId = "email-send")
    public void consumerTransaction(String id) {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("confirm-email");
        stream = stream.filter((transactionId, value) -> id.equalsIgnoreCase(transactionId));

        stream.foreach((transactionId, value) -> {
            System.out.println(transactionId);
            System.out.println(value);
        });

    }
}
