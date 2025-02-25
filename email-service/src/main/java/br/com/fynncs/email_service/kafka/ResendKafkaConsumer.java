package br.com.fynncs.email_service.kafka;

import br.com.fynncs.email_service.comum.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ResendKafkaConsumer {

    public static void deleteAndResend(Map<String, Map<Integer, List<ConsumerRecord<String, String>>>> map,
                                       Class<?> clazz, KafkaProducer kafkaProducer) throws JsonProcessingException {
        for (Map.Entry<String, Map<Integer, List<ConsumerRecord<String, String>>>> entry : map.entrySet()) {
            for (Map.Entry<Integer, List<ConsumerRecord<String, String>>> listEntry : entry.getValue().entrySet()) {
                AtomicReference<Long> maxOffset = new AtomicReference<>(0L);
                listEntry.getValue().parallelStream().map(ConsumerRecord::offset).max(Long::compareTo).ifPresent(maxOffset::set);
                KafkaTopicManager.deleteMessage(entry.getKey(), listEntry.getKey(), maxOffset.get().intValue());
                for (ConsumerRecord<String, String> record : listEntry.getValue()) {
                    kafkaProducer.sendConfirmEmail(record.key(), ObjectMapper.readValue(record.value(), clazz));
                }
            }
        }
    }

    public static void resend(Map<String, Map<Integer, List<ConsumerRecord<String, String>>>> map,
                              Class<?> clazz, KafkaProducer kafkaProducer) throws JsonProcessingException {
        for (Map.Entry<String, Map<Integer, List<ConsumerRecord<String, String>>>> entry : map.entrySet()) {
            for (Map.Entry<Integer, List<ConsumerRecord<String, String>>> listEntry : entry.getValue().entrySet()) {
                for (ConsumerRecord<String, String> record : listEntry.getValue()) {
                    kafkaProducer.sendConfirmEmail(record.key(), ObjectMapper.readValue(record.value(), clazz));
                }
            }
        }
    }
}
