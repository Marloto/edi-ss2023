package de.thi.informatik.edi.highscore.model;

public class Player {
	private Long id;
	private String name;

	public Player() {
	}

	public Player(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + "}";
	}
}
