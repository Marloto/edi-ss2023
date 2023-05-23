package de.thi.informatik.edi.highscore.model;

import java.util.List;
import java.util.TreeSet;

public class Leaderboard {

	private TreeSet<Enriched> elements = new TreeSet<>();
	
	public Leaderboard() {
	}

	public Leaderboard(List<Enriched> readValue) {
		this.elements.addAll(readValue);
	}

	public Leaderboard update(Enriched value) {
		elements.add(value);
		if (elements.size() > 3) {
			elements.remove(elements.last());
		}
		return this;
	}

	public List<Enriched> toList() {
		return List.copyOf(elements);
	}

}
