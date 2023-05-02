package de.thi.informatik.edi.sensor;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class DataEventService {
	
	@Value("${mqtt.topic}")
	private String topic;

	private MessageBrokerService service;
	
	private static final Pattern p = Pattern.compile("servers/(.*?)/(.*)");

	public DataEventService(@Autowired MessageBrokerService service) {
		this.service = service;
	}
	
	public Flux<DataEvent> events() {
		return this.service.create(topic)
			.map(el -> Tuples.of(p.matcher(el.getT1()), el.getT2()))
			.filter(el -> el.getT1().find())
			.map(el -> new DataEvent(
					el.getT1().group(1), 
					el.getT1().group(2),
					Double.parseDouble(el.getT2())));
	}
	
	public Flux<DataEvent> eventsByServer(String server) {
		return this.events().filter(el -> server.equals(el.getServer()));
	}
	
	public Flux<DataEvent> eventsByType(String type) {
		return this.events().filter(el -> type.equals(el.getType()));
	}
	
	// -> .map(this::transform);
//	public DataEvent transform(Tuple2<String, String> data) {
//		String[] split = data.getT1().split("/");
//		String server = split[1];
//		String type = split[2];
//		if(split.length >= 4) {
//			type += "/" + split[3];
//		}
//		double parseDouble = Double.parseDouble(data.getT2());
//		return new DataEvent(server, type, parseDouble);
//	}
}
