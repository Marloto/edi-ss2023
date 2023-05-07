package de.thi.informatik.edi.rxdata.dto;

import java.util.UUID;

import de.thi.informatik.edi.rxdata.model.Person;

public class PersonResponse {
	private UUID id;
	private String name;
	
	public PersonResponse() {
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public static PersonResponse fromPerson(Person person) {
		PersonResponse dto = new PersonResponse();
		dto.id = person.getId();
		dto.name = person.getName();
		return dto;
	}
}
