package de.thi.informatik.edi.shop.shopping.services;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.shopping.services.messages.StockChangeMessage;

@Service
public class StockChangeMessageConsumerService extends MessageConsumerService {

	private static Logger logger = LoggerFactory.getLogger(StockChangeMessageConsumerService.class);
	
	@Value("${kafka.stockTopic:stock-changes}")
	private String topic;
	
	private StockService stocks;
	
	public StockChangeMessageConsumerService(@Autowired StockService stocks) {
		this.stocks = stocks;
	}
	
	protected void handle(ConsumerRecord<String, String> el) {
		String value = el.value();
		logger.info("Received message " + value);
		try {
			StockChangeMessage message = new ObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(value, StockChangeMessage.class);
			logger.info("Update article " + el.key() + " to stock " + message.getValue());
			UUID article = UUID.fromString(el.key());
			stocks.updateStock(article, message.getValue());
		} catch (Exception e) {
			logger.error("Error while handling message", e);
		}
	}
	
	protected String getClientId() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName() + "-stock";
	}
	
	protected String getTopic() {
		return topic;
	}
}
