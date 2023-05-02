package de.thi.informatik.edi.rxdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	private SocketHandler handler;

	public WebSocketConfig(@Autowired SocketHandler handler) {
		this.handler = handler;
	}

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler, "/data");
	}
}