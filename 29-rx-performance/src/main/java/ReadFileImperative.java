import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ReadFileImperative {
	private static final List<Long> results = new ArrayList<>();
	private static Consumer<String> DEV_NULL = (res) -> {};
	private static Consumer<String> CONSOLE = System.out::println;
	public static void main(String[] args) {
		// dry-run and start 10 rounds
		doReadFile(DEV_NULL, () -> doRun(DEV_NULL, 1000));
	}
	
	private static void doFinish(int n, long all) {
		long overall = 0;
		long min = Long.MAX_VALUE;
		long max = 0;
		long count = 0;

		for(Long time : results) {
			overall += time;
			max = Math.max(max, time);
			min = Math.min(min, time);
			count ++;
			System.out.println("Res: " + time + "ns");
		}
		System.out.println("---");
		System.out.println("Count: " + count);
		System.out.println("Max: " + TimeUnit.NANOSECONDS.toMillis(max) + "ms");
		System.out.println("Min: " + TimeUnit.NANOSECONDS.toMillis(min) + "ms");
		System.out.println("Res (avg.): " + TimeUnit.NANOSECONDS.toMillis(overall / count) + "ms");
		System.out.println("All Time: " + TimeUnit.NANOSECONDS.toMillis(all) + "ms");
		System.out.println("All Time per Count: " + TimeUnit.NANOSECONDS.toMillis(all / count) + "ms");
	}

	private static void doRun(Consumer<String> handle, int n) {
		doRun(handle, n, System.nanoTime());
	}
	
	private static void doRun(Consumer<String> handle, int n, long runStart) {
		for(int i = 0; i < n; i ++) {
			long start = System.nanoTime();
			doReadFile(DEV_NULL, () -> {
				long end = System.nanoTime();				
				long time = end - start;
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
		new Thread(() -> {
			try(BufferedReader reader = new BufferedReader(new FileReader("prices.csv"))) {
				String line = reader.readLine();
				LocalDate lastMonth = null;
				double sum = 0;
				int count = 0;
				
				while ((line = reader.readLine()) != null) {
					String[] split = line.split(",");
					if(split.length >= 2) {
						LocalDate date = LocalDate.parse(split[0]);
						double value = Double.parseDouble(split[1]);
						if(lastMonth == null || lastMonth.getMonthValue() == date.getMonthValue()) {						
							sum += value;
							count ++;
						} else {
							cons.accept(date.getMonth().name() + ": " + (sum / count));
							sum = value;
							count = 1;
						}
						lastMonth = date;
					}
				}
				if(count > 0) {				
					cons.accept(lastMonth.getMonth().name() + ": " + (sum / count));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			complete.run();
		}).start();
	}
}
