package de.thi.informatik.edi.pubsub;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.thi.informatik.edi.pubsub.manager.ChangeManager;
import de.thi.informatik.edi.pubsub.manager.Observer;

@Component
public class SubscriptionManager {
	@Autowired
	public ChangeManager manager;
	@Autowired
	private ChannelManager channels;
	
	private Map<String, Deque<String>> buffer = new HashMap<>();
	
	public void register(String topic, String clientId) {
		// TODO same client with two topics
		if(!buffer.containsKey(clientId)) {
			buffer.put(clientId, new LinkedList<>());
		}
		this.manager.register(channels.getOrCreate(topic), new Observer() {
			public void update(String message) {
				buffer.get(clientId).add(message);
			}
		});
	}

	public List<String> getMessages(String topic, String client) {
		List<String> resp = new ArrayList<>();
		Deque<String> list = buffer.get(client);
		if(list != null) {
			while(list.size() > 0) {
				resp.add(list.removeFirst());
			}
		}
		return resp;
	}
}
