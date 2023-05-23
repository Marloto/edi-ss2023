package de.thi.informatik.edi.shop.shopping.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class SomeService {
	
	@Value("${cart.topic:cart}")
	private String topic;
	
	private OtherService other;

	public SomeService(@Autowired OtherService other) {
		this.other = other;
	}
	
	@PostConstruct
	public void init() {
		
	}
	
	@PreDestroy
	public void clear() {
		
	}
}
