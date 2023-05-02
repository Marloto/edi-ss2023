package de.thi.informatik.edi.rxdata;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SocketHandler extends TextWebSocketHandler {
	
	List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
	
	private CollectData data;
	
	public SocketHandler(@Autowired CollectData data) {
		this.data = data;
	}
	
	private String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}
	
	private void send(WebSocketSession session, String data) {
		try {
			if(session.isOpen()) {				
				session.sendMessage(new TextMessage(data));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleTextMessage(WebSocketSession session, TextMessage message)
			throws InterruptedException, IOException {
		
	}

	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}
}
