package de.thi.informatik.edi.pubsub;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.thi.informatik.edi.pubsub.manager.ChangeManager;
import de.thi.informatik.edi.pubsub.manager.Channel;

@Component
public class ChannelManager {
	@Autowired
	private ChangeManager manager;
	
	private Map<String, Channel> channels = new HashMap<>();
	
	public Channel getOrCreate(String topic) {
		if(!channels.containsKey(topic)) {
			channels.put(topic, manager.create(topic));
		}
		return channels.get(topic);
	}
}
