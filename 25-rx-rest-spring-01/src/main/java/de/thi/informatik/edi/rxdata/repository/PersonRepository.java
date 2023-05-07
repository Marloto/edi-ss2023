package de.thi.informatik.edi.rxdata.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import de.thi.informatik.edi.rxdata.model.Person;

public interface PersonRepository extends ReactiveCrudRepository<Person, UUID> {

}
