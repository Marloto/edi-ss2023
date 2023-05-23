package de.thi.informatik.edi.highscore.model;

public class BodyTemp implements Vital {
	private String timestamp;
	private Double temperature;
	private String unit;
	
	public BodyTemp() {
	}
	
	public BodyTemp(String timestamp, Double temperature, String unit) {
		this.timestamp = timestamp;
		this.temperature = temperature;
		this.unit = unit;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public Double getTemperature() {
		return this.temperature;
	}

	public String getUnit() {
		return this.unit;
	}

	@Override
	public String toString() {
		return "{" + " timestamp='" + getTimestamp() + "'" + ", temperature='" + getTemperature() + "'" + ", unit='"
				+ getUnit() + "'" + "}";
	}
}