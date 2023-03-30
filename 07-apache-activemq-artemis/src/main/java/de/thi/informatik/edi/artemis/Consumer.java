package de.thi.informatik.edi.artemis;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	@JmsListener(destination = "${jms.queue.destination}")
	public void receive(String msg) {
		System.out.println("Got Message: " + msg);
	}
}