package de.thi.informatik.edi.rxdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementValue {
	private long time;
	private double value;

	public long getTime() {
		return time;
	}

	public double getValue() {
		return value;
	}

	public String toString() {
		return "Value [time=" + time + ", value=" + value + "]";
	}
	
}
