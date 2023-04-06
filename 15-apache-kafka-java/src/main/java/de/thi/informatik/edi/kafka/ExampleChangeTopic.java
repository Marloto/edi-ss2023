package de.thi.informatik.edi.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreatePartitionsResult;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.common.KafkaFuture;

public class ExampleChangeTopic {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		try(Admin admin = Admin.create(properties)) {
			Map<String,NewPartitions> map = new HashMap<>();
			map.put("test-topic", NewPartitions.increaseTo(2));
			CreatePartitionsResult result = admin.createPartitions(map);
			KafkaFuture<Void> future = result.values().get("test-topic");
			future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
	}
}
