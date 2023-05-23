package de.thi.informatik.edi.highscore;

import java.io.IOException;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.highscore.model.BodyTemp;
import de.thi.informatik.edi.highscore.model.CombinedVitals;
import de.thi.informatik.edi.highscore.model.Pulse;

public class JsonSerdes {
	
	private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	public static Serde<Pulse> pulse() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, Pulse.class));
	}

	public static Serde<BodyTemp> bodyTemp() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, BodyTemp.class));
	}

	public static Serde<CombinedVitals> combinedVitals() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, CombinedVitals.class));
	}

	private static <T> T readValue(byte[] data, Class<T> clazz) {
		try {
			return mapper.readValue(data, clazz);
		} catch (IOException e) {
			return null;
		}
	}
	
	private static <T> byte[] writeValue(T data) {
		try {
			return mapper.writeValueAsBytes(data);
		} catch (IOException e) {
			return new byte[0];
		}
	}
	
	public static class JsonSerializer implements Serializer<Object> {
		public byte[] serialize(String topic, Object data) {
			return JsonSerdes.writeValue(data);
		}
	}

}
