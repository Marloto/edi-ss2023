package de.thi.informatik.edi.rxdata.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.rxdata.model.Person;
import de.thi.informatik.edi.rxdata.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

	private PersonRepository persons;

	public PersonService(@Autowired PersonRepository persons) {
		this.persons = persons;
	}

	public Flux<Person> listAll() {
		return this.persons.findAll();
	}

	public Mono<Person> getById(UUID id) {
		return this.persons.findById(id);
	}

	public Mono<Person> create(String name) {
		return this.persons.save(new Person(name));
	}
}
