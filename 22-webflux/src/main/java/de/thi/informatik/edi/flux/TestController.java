package de.thi.informatik.edi.flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {
	@GetMapping
	public Flux<Integer> getAllEmployees() {
	    return Flux.fromArray(new Integer[] {1, 2, 2, 1, 3, 2, 4});
	}
}
