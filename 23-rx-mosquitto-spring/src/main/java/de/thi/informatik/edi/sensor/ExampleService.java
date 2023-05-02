package de.thi.informatik.edi.sensor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
	private DataEventService service;
	
	public ExampleService(@Autowired DataEventService service) {
		this.service = service;
	}
	@PostConstruct
	public void init() {
		this.service.eventsByServer("990c4410fb4a").subscribe(System.out::println);
	}
}


/**
servers/990c4410fb4a/cpu-usage 0.01005025125628145
servers/990c4410fb4a/cpu-free 0.9899497487437185
servers/990c4410fb4a/free-mem 6982.91015625
servers/990c4410fb4a/process/usage-mem 56037376
servers/990c4410fb4a/process/heap-total 17932288
servers/990c4410fb4a/total-mem 7959.4609375
servers/990c4410fb4a/freemem-percentage 0.877309432269577
**/