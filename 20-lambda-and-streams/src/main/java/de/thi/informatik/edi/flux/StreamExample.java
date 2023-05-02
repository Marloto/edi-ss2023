package de.thi.informatik.edi.flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExample {
	public static void main(String[] args) {
		//List.of("1", "21", "2", "4", "3", "42").forEach(System.out::println);
		Stream<String> stream = List.of("1", "21", "2", "4", "3", "42").stream();
		// Create Operator
		// Intermediate Operatoren
		//stream.map(el -> Integer.parseInt(el));
		Stream<Integer> map = stream.map(Integer::parseInt);
		// ...
		Stream<Integer> filter = map.filter(el -> el > 3);
		// Terminal
		filter.forEach(System.out::println);
//		map.forEach(System.out::println);
		
		List.of("1", "21", "2", "4", "3", "42").stream()
			.map(Integer::parseInt)
			.filter(el -> el > 3)
			.forEach(System.out::println);
		
		List<Integer> result = List.of("1", "21", "2", "4", "3", "42").stream()
			.map(Integer::parseInt)
			.filter(el -> el > 3)
			.collect(Collectors.toList());
		
		// Performance?
		// Was bringt dies?
		// - Code ist "Lesend"
		// - Reactive Streams hätte Transformer
		// - Unicast -> reactive ist das nicht zwingend so
		// - blockierend / asynchron?
	}
}
