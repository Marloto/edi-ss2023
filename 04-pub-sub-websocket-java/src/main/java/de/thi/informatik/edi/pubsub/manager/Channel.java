package de.thi.informatik.edi.pubsub.manager;

public class Channel {
	private String topic;
	private ChangeManager manager;
	
	public Channel(String topic, ChangeManager manager) {
		this.topic = topic;
		this.manager = manager;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void notify(String message) {
		this.manager.notify(this, message);
	}
}
