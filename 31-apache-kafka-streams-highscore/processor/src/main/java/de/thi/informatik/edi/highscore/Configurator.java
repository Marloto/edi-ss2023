package de.thi.informatik.edi.highscore;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configurator {
	private static Logger logger = LoggerFactory.getLogger(Configurator.class);
	
	private static final int PARTITIONS = 10;
	public static final String PRODUCTS = "products";
	public static final String PLAYERS = "players";
	public static final String SCORE_EVENTS = "score-events";
	public static final String HIGH_SCORES = "high-scores";
	public static final String LEADER_BOARDS = "leader-boards";
	public static final String APP_ID = "dev1";

	public static void main(String[] args) {
		List<String> topics = Arrays.asList(PRODUCTS, PLAYERS, SCORE_EVENTS, HIGH_SCORES, LEADER_BOARDS);
		
		Properties properties = new Properties();
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		try(Admin admin = Admin.create(properties)) {
			ListTopicsResult result = admin.listTopics();
			Set<String> set = result.names().get();
			// Remove APP-ID topics, auto generated stuff
			for(String topic : set) {
				if(topic.startsWith(APP_ID + "-")) {
					logger.info("Found " + topic + " ... remove!");
					DeleteTopicsResult res = admin.deleteTopics(Arrays.asList(topic));
					res.all().get();
				}
			}
			// Recreate topics
			for(String topic : topics) {
				if(set.contains(topic)) {
					logger.info("Found " + topic + " ... remove!");
					DeleteTopicsResult res = admin.deleteTopics(Arrays.asList(topic));
					res.all().get();
				}
			}
			
			Thread.sleep(1000); // wait a little
				
			for(String topic : topics) {
				logger.info("Recreate " + topic + " with " + PARTITIONS + " partitions");
				NewTopic t = new NewTopic(topic, PARTITIONS, (short) 1);
				CreateTopicsResult res = admin.createTopics(Arrays.asList(t));
				res.values().get(topic);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
