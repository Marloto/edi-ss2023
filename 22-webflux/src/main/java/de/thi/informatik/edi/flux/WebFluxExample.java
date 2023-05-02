package de.thi.informatik.edi.flux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

public class WebFluxExample {
	public static void main(String[] args) throws IOException {
//		Mono<String> mono = Mono.just("42");
//		Flux<String> flux = Flux.just("1", "3", "2");
//		
//		mono.subscribe(System.out::println);
//		flux.subscribe(System.out::println);
//		System.out.println("Finish!");
//		
//		String block = mono.block();
//		List<String> list = flux.collectList().block();
//		String blockLast = flux.blockLast();
		
//		Flux
//			.interval(Duration.ofSeconds(1))
//			.subscribe(System.out::println);
		
		// Ist interval hot od. cold? -> cold, startet
		// nicht, bevor nicht eine subscription vorliegt
//		Flux
//			.interval(Duration.ofSeconds(1))
//			.map(el -> {
//				System.out.println("cold?" + el);
//				return el;
//			});
		
//		Flux.fromArray(new Integer[] {1,2,3,4})
//			.subscribe(System.out::println);
		
//		Flux<String> create = Flux.<String>create(sink -> {
//			System.out.println("Hello sink...");
//			sink.next("String");
//		});
//		
//		create.subscribe(System.out::println);
//		create.subscribe(System.out::println);
		
		
		
		// interval ruft nie onComplete auf, daher kommt das
		// Ergebnis nicht, blockierender Aufruf
//		Long res = Flux
//			.interval(Duration.ofSeconds(1))
//			.blockLast();
//		System.out.println(res);
		
//		Flux<String> doSomething = doSomething();
//		doSomething.subscribe();
		
//		Many<Object> many = Sinks.many().multicast().onBackpressureBuffer();
//		Flux<Object> flux = many.asFlux();
		
		// Sinks.empty() -> um eine Senke zu erzeugen, die 
		// nichts liefert, ggf. noch einmal prüfen	
		
//		new Thread(() -> {
//			many.tryEmitNext("Test1");
//			many.tryEmitNext("Test2");
//		}).start();
//		
//		// ... irgendwann normal mit dem Flux arbeiten
//		flux.subscribe(System.out::println);
//		flux.subscribe(System.out::println);
//		
		
//		Flux.fromArray(new Integer[] {1,2,3,4})
//			.filter(el -> el > 2)
//			.subscribe(System.out::println);
//		
//		Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
//		interval
//			.map(el -> "el: " + el)
//			.subscribe(System.out::println);
//		interval
//			.sample(Duration.ofSeconds(3))
//			.subscribe(System.out::println);
		
//		Flux.fromArray(new Integer[] {1, 2, 2, 1, 3, 2, 4})
//		    .distinct()
//		    .subscribe(System.out::println);
		
//		Flux.interval(Duration.ofSeconds(1))
//		    .skip(2)
//		    .subscribe(System.out::println);
		
//		Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
//		Flux<String> map = interval.filter(el -> el % 2 == 0).map(el -> String.valueOf(el));
//		map.subscribe(System.out::println);
		
		//Flux.just(null, null).subscribe(System.out::println);
		
//		Flux.interval(Duration.ofSeconds(1))
//			.map(el -> Math.random())
//			.filter(el -> el > 0.5)
//			.subscribe(System.out::println);
		
//		Flux.fromArray(new Integer[] {1, 2, 2, 1, 3, 2, 4})
//			.reduce((a, b) -> a + b)
//			.subscribe(System.out::println);
		
//		Flux.concat(
//		        Flux.fromArray(new Integer[] {1, 2, 2, 1, 3, 2, 4}), 
//		        Flux.fromArray(new Integer[] {9, 8, 8, 9, 7, 8, 6})
//		).subscribe(System.out::print);
		
//		Flux.concat(
//				Flux.fromArray(new Integer[] {1, 2, 2, 1, 3, 2, 4}), 
//				Flux.interval(Duration.ofSeconds(1))
//		).subscribe(System.out::print);
		
//		Flux.merge(
//				Flux.fromArray(new Integer[] {1, 2, 2, 1, 3, 2, 4}).map(el -> 'A'), 
//		        Flux.interval(Duration.ofSeconds(1)).map(el -> 'B')
//		).subscribe(System.out::println);
		
		Flux<Double> a = Flux.just(1.0, 5.0, 2.0, 3.0);
		Flux<Double> b = Flux.just(7.0, 2.0, 4.0, 9.0);
		Flux.zip(a, b).map(el -> el.getT1() + el.getT2()).subscribe(System.out::println);
		
		System.in.read();
	}
	
	
	public static Flux<String> doSomething() {
		return Flux.<String>create(sink -> {
			System.out.println("Hello sink...");
			sink.next("String");
		}); // zzgl. map, filter, usw.
	}
}
