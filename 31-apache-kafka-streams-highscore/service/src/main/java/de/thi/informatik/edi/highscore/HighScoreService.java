package de.thi.informatik.edi.highscore;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.highscore.model.Enriched;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

@Service
public class HighScoreService extends MessageConsumerService {
	
	private Map<String, List<Enriched>> last;
	
	private static final ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@PostConstruct
	private void init() {
		last = new HashMap<>();
		this.getMessages()
			.map(el -> Tuples.of(
				el.getT1(), 
				Arrays.asList(readValue(el.getT2().getBytes(), Enriched[].class))))
			.doOnNext(el -> System.out.println("Update "  +el.getT1() + " to " + el.getT2().toString()))
			.subscribe(el -> last.put(el.getT1(), el.getT2()));
	}
	
	private static <T> T readValue(byte[] data, Class<T> clazz) {
		try {
			return mapper.readValue(data, clazz);
		} catch (IOException e) {
			return null;
		}
	}
	
	public Flux<Enriched> get(Long id) {
		return Flux.fromIterable(this.last.get(id.toString()));
	}
	
	@Override
	protected String getClientId() throws UnknownHostException {
		return "dev2";
	}

	@Override
	protected String getTopic() {
		return "high-scores";
	}

}
