package de.thi.informatik.edi.pubsub;

public class Main {
	public static void main(String[] args) {
		ChangeManager manager = new SimpleChangerManager();
		
		Channel channel1 = manager.create("test1");
		Channel channel2 = manager.create("test2");
		
		manager.register(channel1, new Observer() {
			public void update(String message) {
				System.out.println(message);
			}
		});
		
		channel1.notify("Hello, World!");
		channel2.notify("Hello, Other!");
	}
}
