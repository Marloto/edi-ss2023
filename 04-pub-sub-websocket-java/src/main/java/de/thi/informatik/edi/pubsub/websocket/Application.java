package de.thi.informatik.edi.pubsub.websocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.glassfish.tyrus.server.Server;

public class Application {

	public static void main(String[] args) {
		Server server;
		server = new Server("localhost", 7071, "/", new HashMap<>(), EndPoint.class);
		try {
			server.start();
			System.out.println("--- server is running");
			System.out.println("--- press any key to stop the server");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			bufferRead.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}
}
