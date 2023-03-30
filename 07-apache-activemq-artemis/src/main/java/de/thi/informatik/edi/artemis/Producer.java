package de.thi.informatik.edi.artemis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {
	@Autowired
	JmsTemplate jmsTemplate;

	@Value("${jms.queue.destination}")
	private String destinationQueue;

	public void send(String msg) {
		jmsTemplate.convertAndSend(destinationQueue, msg);
	}
}