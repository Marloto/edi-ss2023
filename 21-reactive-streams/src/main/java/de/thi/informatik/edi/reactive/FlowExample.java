package de.thi.informatik.edi.reactive;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class FlowExample {
	private static class EndSubscriber<T> implements Subscriber<T> {
		private Subscription subscription;
		public List<T> consumedElements = new LinkedList<>();
		private boolean done;

		public void onSubscribe(Subscription subscription) {
			this.subscription = subscription;
			subscription.request(1);
		}

		public void onNext(T item) {
			System.out.println("Got : " + item);
			consumedElements.add(item);
			subscription.request(1);
		}

		public void onError(Throwable t) {
			t.printStackTrace();
		}

		public void onComplete() {
			this.done = true;
		}

		public boolean isDone() {
			return done;
		}
	}

	public static class TransformProcessor<T, R> extends SubmissionPublisher<R> implements Processor<T, R> {

		private Function<T, R> function;
		private Flow.Subscription subscription;

		public TransformProcessor(Function<T, R> function) {
			super();
			this.function = function;
		}

		@Override
		public void onSubscribe(Subscription subscription) {
			this.subscription = subscription;
			subscription.request(1);
		}

		@Override
		public void onNext(T item) {
			submit(function.apply(item));
			subscription.request(1);
		}

		@Override
		public void onError(Throwable t) {
			t.printStackTrace();
		}

		@Override
		public void onComplete() {
			close();
		}
	}

	public static void main(String[] args) throws Exception {
		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
		
		TransformProcessor<String, Integer> transformProcessor = new TransformProcessor<>(Integer::parseInt);
		TransformProcessor<Integer, Long> transformProcessor2 = new TransformProcessor<>(Long::valueOf);
		EndSubscriber<Long> subscriber = new EndSubscriber<>();
		EndSubscriber<Integer> subscriber2 = new EndSubscriber<>();
		
		publisher.subscribe(transformProcessor);
		transformProcessor.subscribe(transformProcessor2);
		transformProcessor.subscribe(subscriber2);
		transformProcessor2.subscribe(subscriber);
		
		List.of("1", "21", "2", "4", "3", "42").forEach(publisher::submit);
		publisher.close();

		while (!subscriber.isDone()) {
			Thread.sleep(100);
		}
	}
}