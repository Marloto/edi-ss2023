package de.thi.informatik.edi.rxdata.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.rxdata.dto.PersonRequest;
import de.thi.informatik.edi.rxdata.dto.PersonResponse;
import de.thi.informatik.edi.rxdata.service.PersonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/person")
public class PersonEndpoint {
    private PersonService persons;

    public PersonEndpoint(@Autowired PersonService persons) {
        this.persons = persons;
    }

    @GetMapping
    public Flux<PersonResponse> listAll() {
        return this.persons.listAll()
                .map(PersonResponse::fromPerson);
    }

    @GetMapping("/{id}")
    public Mono<PersonResponse> getById(@PathVariable UUID id) {
        return this.persons.getById(id)
                .map(PersonResponse::fromPerson);
    }

    @PostMapping()
    public Mono<PersonResponse> getById(@RequestBody PersonRequest person) {
        return this.persons.create(person.getName())
                .map(PersonResponse::fromPerson);
    }
}
