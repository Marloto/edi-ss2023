package de.thi.informatik.edi.artemis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class Endpoint {
	@Autowired
	Producer producer;

	@GetMapping(value = "/publish/{msg}")
	public String produce(@PathVariable("msg") String msg) {
		producer.send(msg);
		return "Done";
	}
}