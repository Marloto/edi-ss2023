package de.thi.informatik.edi.rxdata;

class Measurement {
	private double temperature;
	private double humidity;
	
	public double getHumidity() {
		return humidity;
	}
	
	public double getTemperature() {
		return temperature;
	}

	@Override
	public String toString() {
		return "Measurements [temperature=" + temperature + ", humidity=" + humidity + "]";
	}
	
	public static Measurement fromMeasurementValue(MeasurementValue temp, MeasurementValue humi) {
		Measurement m = new Measurement();
		m.humidity = humi.getValue();
		m.temperature = temp.getValue();
		return m;
	}
}