import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

public class ReadFileReactive {
	private static final List<Long> results = new ArrayList<>();
	private static Consumer<String> DEV_NULL = (res) -> {};
	private static Consumer<String> CONSOLE = System.out::println;

	public static void main(String[] args) throws IOException {
		// dry-run and start 10 rounds
		doReadFile(DEV_NULL, () -> doRun(DEV_NULL, 1000));
		System.in.read();
	}

	private static void doFinish(int n, long all) {
		while (results.size() < n) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long overall = 0;
		long min = Long.MAX_VALUE;
		long max = 0;
		long count = 0;

		for (Long time : results) {
			overall += time;
			max = Math.max(max, time);
			min = Math.min(min, time);
			count ++;
		}
		System.out.println("---");
		System.out.println("Count: " + count);
		System.out.println("Max: " + TimeUnit.NANOSECONDS.toMillis(max) + "ms");
		System.out.println("Min: " + TimeUnit.NANOSECONDS.toMillis(min) + "ms");
		System.out.println("Res (avg.): " + TimeUnit.NANOSECONDS.toMillis(overall / count) + "ms");
		System.out.println("All Time: " + TimeUnit.NANOSECONDS.toMillis(all) + "ms");
		System.out.println("All Time per Count: " + TimeUnit.NANOSECONDS.toMillis(all / count) + "ms");
		System.exit(0);
	}

	private static void doRun(Consumer<String> handle, int n) {
		doRun(handle, n, System.nanoTime());
	}

	private static void doRun(Consumer<String> handle, int n, long runStart) {
		for(int i = 0; i < n; i ++) {
			//System.out.println("Start " + (max - n + 1) + "/" + max + " at " + (System.nanoTime() - runStart) / 1000 / 1000 + "ms");
			long start = System.nanoTime();
			doReadFile(handle, () -> {
				long end = System.nanoTime();
				long time = end - start;
				//System.out.println("Finished " + (max - n + 1) + "/" + max + " with " + time / 1000 / 1000 + "ms");
				synchronized (results) {
					results.add(time);
					if(results.size() == n) {
						doFinish(n, (end - runStart));
					}
				}
			});
		}
	}

	private static void doReadFile(Consumer<String> cons, Runnable complete) {
		AtomicBoolean count = new AtomicBoolean();
		Flux.using(() -> new FileReader("prices.csv"),
				reader -> Flux.fromStream(new BufferedReader(reader).lines()), 
				reader -> {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				})
				.publishOn(Schedulers.parallel())
				.map(el -> el.split(",")).filter(el -> el.length >= 2)
				.map(el -> Tuples.of(LocalDate.parse(el[0]), Double.parseDouble(el[1]), 1))
				.bufferUntilChanged(el -> (el.getT1().getYear() << 4) | el.getT1().getMonthValue())
				.flatMap(buf -> Flux.fromIterable(buf).publishOn(Schedulers.parallel())
						.reduce((a, b) -> Tuples.of(a.getT1(), a.getT2() + b.getT2(), a.getT3() + b.getT3())))
				.map(el -> el.getT1().getMonth().name() + ": " + (el.getT2() / el.getT3()))
				.subscribe(cons, el -> {}, () -> {
					if(!count.getAndSet(true)) {
						complete.run();
					}
				}); // complete is called multiple times
	}
}
