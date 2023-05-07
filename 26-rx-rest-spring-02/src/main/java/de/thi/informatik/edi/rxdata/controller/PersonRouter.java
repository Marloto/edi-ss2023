package de.thi.informatik.edi.rxdata.controller;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import de.thi.informatik.edi.rxdata.dto.PersonRequest;
import de.thi.informatik.edi.rxdata.dto.PersonResponse;
import de.thi.informatik.edi.rxdata.service.PersonService;

@Configuration
public class PersonRouter {
	@Bean
	public RouterFunction<ServerResponse> compose(PersonService service) {
		return
				route(GET("/api/v1/person"), 
						req -> ok().body(service.listAll().map(PersonResponse::fromPerson), PersonResponse.class))
				.and(route(GET("/api/v1/person/{id}"), 
						req -> ok().body(service.getById(UUID.fromString(req.pathVariable("id"))).map(PersonResponse::fromPerson), PersonResponse.class)))
				.and(route(POST("/api/v1/person"), 
						req -> req.body(toMono(PersonRequest.class)).doOnNext(el ->service.create(el.getName())).then(ok().build())));
	}
}
