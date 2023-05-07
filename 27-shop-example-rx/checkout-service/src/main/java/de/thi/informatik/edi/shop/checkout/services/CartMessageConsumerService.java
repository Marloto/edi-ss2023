package de.thi.informatik.edi.shop.checkout.services;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.thi.informatik.edi.shop.checkout.services.messages.ArticleAddedToCartMessage;
import de.thi.informatik.edi.shop.checkout.services.messages.CartMessage;
import de.thi.informatik.edi.shop.checkout.services.messages.CreatedCartMessage;
import de.thi.informatik.edi.shop.checkout.services.messages.DeleteArticleFromCartMessage;

@Service
public class CartMessageConsumerService extends MessageConsumerService {
	
	private static Logger logger = LoggerFactory.getLogger(CartMessageConsumerService.class);

	@Value("${kafka.cartTopic:cart}")
	private String topic;
	
	private ShoppingOrderService orders;
	
	public CartMessageConsumerService(@Autowired ShoppingOrderService orders, @Autowired TaskExecutor executor) {
		super(executor);
		this.orders = orders;
	}
	
	protected void handle(ConsumerRecord<String, String> el) {
		String value = el.value();
		logger.info("Received message " + value);
		try {
			CartMessage message = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(value, CartMessage.class);
			if(message instanceof ArticleAddedToCartMessage) {
				logger.info("Article added to cart " + ((ArticleAddedToCartMessage) message).getId());
				this.orders.addItemToOrderByCartRef(
						((ArticleAddedToCartMessage) message).getId(), 
						((ArticleAddedToCartMessage) message).getArticle(), 
						((ArticleAddedToCartMessage) message).getName(), 
						((ArticleAddedToCartMessage) message).getPrice(), 
						((ArticleAddedToCartMessage) message).getCount());
			} else if(message instanceof DeleteArticleFromCartMessage) {
				logger.info("Article removed from cart " + ((DeleteArticleFromCartMessage) message).getId());
				this.orders.deleteItemFromOrderByCartRef(
						((DeleteArticleFromCartMessage) message).getId(), 
						((DeleteArticleFromCartMessage) message).getArticle());
			} else if(message instanceof CreatedCartMessage) {
				logger.info("Cart created " + ((CreatedCartMessage) message).getId());
				this.orders.createOrderWithCartRef(((CreatedCartMessage) message).getId());
				
			}
		} catch (Exception e) {
			logger.error("Error while handling message", e);
		}
	}
	
	protected String getClientId() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName() + "-cart";
	}
	
	@Override
	protected String getTopic() {
		return topic;
	}
	
}
