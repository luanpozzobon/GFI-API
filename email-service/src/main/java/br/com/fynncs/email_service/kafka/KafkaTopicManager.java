package br.com.fynncs.email_service.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Service
public class KafkaTopicManager {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void deleteRecords(Map<TopicPartition, RecordsToDelete> deleteMap) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        try (AdminClient adminClient = AdminClient.create(props)) {
            adminClient.deleteRecords(deleteMap).all().get();
        } catch (Exception ignored) {

        }
    }

    public static void deleteMessage(String topic, int partition, int offset) {
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        Map<TopicPartition, RecordsToDelete> deleteMap = new HashMap<>() {
            {
                put(topicPartition, RecordsToDelete.beforeOffset(offset + 1));
            }
        };
        KafkaTopicManager.deleteRecords(deleteMap);
    }

    public void createTopic(String topicName, int partitions, short replicationFactor) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (Exception ignored) {

        }
    }
}
