package de.thi.informatik.edi.rxdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

@Service
public class CollectData {
	
	@Value("${temp:http://localhost:3000/temperature}")
	private String tempEndpoint;
	@Value("${temp:http://localhost:3000/humidity}")
	private String humiEndpoint;
	
	private RestTemplateBuilder builder;

	public CollectData(@Autowired RestTemplateBuilder builder) {
		this.builder = builder;
	}
	

	
}
