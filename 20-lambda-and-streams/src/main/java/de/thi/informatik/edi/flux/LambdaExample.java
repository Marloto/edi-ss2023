package de.thi.informatik.edi.flux;

import java.util.ArrayList;
import java.util.List;

public class LambdaExample {
	private static void doSomething(String s) {
		System.out.println(s);
	}
	
	public static interface Calculator {
		int calculate(int a, int b);
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		
		for(String s : list) {
			System.out.println(s);
		}
		
		list.forEach(el -> System.out.println(el));
		list.forEach(System.out::println);
		
		list.forEach(el -> LambdaExample.doSomething(el));
		list.forEach(LambdaExample::doSomething);
		
		Calculator some1 = (a, b) -> a + b;
		Calculator some2 = new Calculator() {
			public int calculate(int a, int b) {
				return a + b;
			}
		};
		
		some1 = (a, b) -> a - b;
		
		// ...
		
		int calculate = some1.calculate(2, 4);
		
	}
}
