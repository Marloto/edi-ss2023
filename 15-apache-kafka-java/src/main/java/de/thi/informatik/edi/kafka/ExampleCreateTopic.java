package de.thi.informatik.edi.kafka;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.TopicConfig;

public class ExampleCreateTopic {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		try(Admin admin = Admin.create(properties)) {
			NewTopic topic = new NewTopic("test-topic", 1, (short) 1);
			Map<String, String> config = new HashMap<>();
			config.put(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(60*60*1000));
			topic.configs(config);
			CreateTopicsResult result = admin.createTopics(Collections.singleton(topic));
			KafkaFuture<Void> future = result.values().get("test-topic");
			future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
