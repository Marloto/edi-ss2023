package de.thi.informatik.edi.pubsub.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.thi.informatik.edi.pubsub.manager.ChangeManager;
import de.thi.informatik.edi.pubsub.manager.Channel;
import de.thi.informatik.edi.pubsub.manager.Observer;
import de.thi.informatik.edi.pubsub.manager.SimpleChangerManager;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


@ServerEndpoint("/{topic}")
public class EndPoint {
	
	// Application Server creates on instance per connection, thus, manage changes
	// in a global way
	private static ChangeManager manager = new SimpleChangerManager();
	private static Map<String, Channel> channels = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("topic") String topic) throws IOException {
    	System.out.println("Connection created for " + topic);
        if(!channels.containsKey(topic)) {
        	channels.put(topic, manager.create(topic));
        }
        manager.register(channels.get(topic), new Observer() {
			public void update(String message) {
				if(session.isOpen()) {
					try {
						session.getBasicRemote().sendText(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
    }

    @OnMessage
    public void echo(String message, @PathParam("topic") String topic) {
        manager.notify(channels.get(topic), message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {

    }
}