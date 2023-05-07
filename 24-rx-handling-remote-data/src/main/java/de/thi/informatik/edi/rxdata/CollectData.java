package de.thi.informatik.edi.rxdata;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;

@Service
public class CollectData {
	
	@Value("${temp:http://localhost:3000/temperature}")
	private String tempEndpoint;
	@Value("${temp:http://localhost:3000/humidity}")
	private String humiEndpoint;
	
	private RestTemplateBuilder builder;

	public CollectData(@Autowired RestTemplateBuilder builder) {
		this.builder = builder;
	}
	
	// Was wäre notwendig um das Ziel zu erreichen?
	// -> Verarbeitung:
	//    - Sammeln von Ereignissen
	//    - Zusammenfassen / Vorverarbeiten
	//    - Weiterleiten an WebSocket
	
	// Wie HTTP-Client?
	public Flux<MeasurementValue> load(String url) {
		MeasurementValue[] values = builder.build().getForObject(url, MeasurementValue[].class);
		// -> irgend eine form von on next, od. pollen um es in einen flux zu schmeißen
		return Flux.fromArray(values);
	}
	
	public Flux<Measurement> find() {
		return Flux.combineLatest(temp(), humi(), (a, b) -> Measurement.fromMeasurementValue(a, b));
	}

	private Flux<MeasurementValue> humi() {
		AtomicLong lastHumi = new AtomicLong();
		return Flux.interval(Duration.ofSeconds(5))
			.map(el -> humiEndpoint)
			.flatMap(this::load)
			.filter(el -> el.getTime() > lastHumi.get()) // vergleich mit vorherigen letzten TimeStamp
			.doOnNext(el -> lastHumi.getAndSet(el.getTime())); // speichern
		
		
		// nur die neuen Elemente
	}

	private Flux<MeasurementValue> temp() {
		AtomicLong lastTemp = new AtomicLong();
		return Flux.interval(Duration.ofSeconds(5))
				.map(el -> tempEndpoint)
				.flatMap(this::load)
				.filter(el -> el.getTime() > lastTemp.get()) // vergleich mit vorherigen letzten TimeStamp
				.doOnNext(el -> lastTemp.getAndSet(el.getTime())); // speichern
	}
	
	/**
	 * [{"time": 1234, "value": 123.000}]
	 */
	
	
}
