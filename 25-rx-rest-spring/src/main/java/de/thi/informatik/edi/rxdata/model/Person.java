package de.thi.informatik.edi.rxdata.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Person {
	@Id
	private UUID id;
	private String name;
	private long created;

	public Person() {
		this.id = UUID.randomUUID();
		this.created = System.currentTimeMillis();
	}

	public Person(String name) {
		this();
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public long getCreated() {
		return created;
	}

}
