package de.thi.informatik.edi.sensor;

public class DataEvent {
	private String server;
	private String type;
	private double value;

	public DataEvent(String server, String type, double value) {
		this.server = server;
		this.type = type;
		this.value = value;
	}
	
	public String getServer() {
		return server;
	}
	
	public String getType() {
		return type;
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return server + " -> " + type + ": " + value;
	}
}
