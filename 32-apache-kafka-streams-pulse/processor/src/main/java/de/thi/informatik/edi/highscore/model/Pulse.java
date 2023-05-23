package de.thi.informatik.edi.highscore.model;

public class Pulse implements Vital {
	private String timestamp;
	
	public Pulse() {
	}
	
	public Pulse(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimestamp() {
		return this.timestamp;
	}
}