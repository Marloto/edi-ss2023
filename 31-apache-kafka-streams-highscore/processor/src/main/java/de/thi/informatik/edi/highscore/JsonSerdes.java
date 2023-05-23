package de.thi.informatik.edi.highscore;

import java.io.IOException;
import java.util.Arrays;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.highscore.model.Enriched;
import de.thi.informatik.edi.highscore.model.Leaderboard;
import de.thi.informatik.edi.highscore.model.Player;
import de.thi.informatik.edi.highscore.model.Product;
import de.thi.informatik.edi.highscore.model.ScoreEvent;
import de.thi.informatik.edi.highscore.model.ScoreWithPlayer;

public class JsonSerdes {
	
	private static final ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static Serde<Player> player() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, Player.class));
	}

	public static Serde<Product> product() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, Product.class));
	}

	public static Serde<ScoreEvent> scoreEvent() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, ScoreEvent.class));
	}

	public static Serde<ScoreWithPlayer> scoreWithPlayer() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, ScoreWithPlayer.class));
	}

	public static Serde<Enriched> enriched() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data), 
				(topic, data) -> readValue(data, Enriched.class));
	}
	
	public static Serde<Leaderboard> leaderboard() {
		return Serdes.serdeFrom(
				(topic, data) -> writeValue(data.toList()), 
				(topic, data) -> new Leaderboard(
						Arrays.asList(readValue(data, Enriched[].class))
				));
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
