package de.thi.informatik.edi.shop.checkout.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thi.informatik.edi.shop.checkout.model.ShoppingOrder;
import de.thi.informatik.edi.shop.checkout.model.ShoppingOrderStatus;
import de.thi.informatik.edi.shop.checkout.repositories.ShoppingOrderRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ShoppingOrderService {
	private ShoppingOrderRepository orders;
	private ShoppingOrderMessageProducerService messages;
	private CartMessageConsumerService cartMessages;
	private PaymentMessageConsumerService paymentMessages;

	public ShoppingOrderService(@Autowired ShoppingOrderRepository orders, 
			@Autowired ShoppingOrderMessageProducerService messages,
			@Autowired CartMessageConsumerService cartMessages,
			@Autowired PaymentMessageConsumerService paymentMessages) {
		this.orders = orders;
		this.messages = messages;
		this.cartMessages = cartMessages;
		this.paymentMessages = paymentMessages;
	}
	
	@PostConstruct
	private void init() {
		// Flux<Something> flux = this.cartMessages.getCartFinished().map(vorverarbeitung)
		// flux.subscribe()
		
		this.cartMessages.getCartCreatedMessages()
			.map(el -> el.getId())
			.subscribe(this::createOrderWithCartRef);
		
		this.cartMessages.getAddedToCartMessages().subscribe(el -> addItemToOrderByCartRef(
				el.getId(), 
				el.getArticle(), 
				el.getName(), 
				el.getPrice(), 
				el.getCount()));
		
		this.cartMessages.getDeletedFromCartMessages().subscribe(el -> deleteItemFromOrderByCartRef(
				el.getId(), 
				el.getArticle()));
		
		// Reactive Umstellung?
		this.paymentMessages.getPaymentMessages()
			.filter(el -> "PAYED".equals(el.getStatus()))
			.map(el -> el.getOrderRef())
			.subscribe(this::updateOrderIsPayed);
	}
	
	public void addItemToOrderByCartRef(UUID cartRef, UUID article, String name, double price, int count) {
		ShoppingOrder order = this.getOrCreate(cartRef);
		order.addItem(article, name, price, count);
		this.orders.save(order);
		
	}
	
	private Optional<ShoppingOrder> findByCartRef(UUID cartRef) {
		return this.orders.findByCartRef(cartRef);
	}

	public void deleteItemFromOrderByCartRef(UUID cartRef, UUID article) {
		ShoppingOrder order = this.getOrCreate(cartRef);
		order.removeItem(article);
		this.orders.save(order);
	}

	public ShoppingOrder createOrderWithCartRef(UUID cartRef) {
		ShoppingOrder order = this.getOrCreate(cartRef);
		this.orders.save(order);
		return order;
	}

	private ShoppingOrder getOrCreate(UUID cartRef) {
		Optional<ShoppingOrder> orderOption = this.findByCartRef(cartRef);
		ShoppingOrder order;
		if(orderOption.isEmpty()) {
			order = new ShoppingOrder(cartRef);
		} else {
			order = orderOption.get();
		}
		return order;
	}

	public void updateOrder(UUID id, String firstName, String lastName, String street, String zipCode,
			String city) {
		ShoppingOrder order = findById(id);
		order.update(firstName, lastName, street, zipCode, city);
		this.messages.orderChanged(order);
		this.orders.save(order);
	}

	private ShoppingOrder findById(UUID id) {
		Optional<ShoppingOrder> optional = this.orders.findById(id);
		if(optional.isEmpty()) {
			throw new IllegalArgumentException("Unknown order with id " + id.toString());
		}
		ShoppingOrder order = optional.get();
		return order;
	}

	public void placeOrder(UUID id) {
		ShoppingOrder order = findById(id);
		if(order.getStatus().equals(ShoppingOrderStatus.CREATED)) {
			order.setStatus(ShoppingOrderStatus.PLACED);
			this.messages.orderChanged(order);
			this.orders.save(order);
		}
	}

	public ShoppingOrder find(UUID id) {
		return this.findById(id);
	}

	public void updateOrderIsPayed(UUID id) {
		ShoppingOrder order = this.findById(id);
		if(order.getStatus() == ShoppingOrderStatus.PLACED) {
			order.setStatus(ShoppingOrderStatus.PAYED);
			this.orders.save(order);
		}
	}

	public void updateOrderIsShipped(UUID id) {
		ShoppingOrder order = this.findById(id);
		if(order.getStatus() == ShoppingOrderStatus.PAYED) {
			order.setStatus(ShoppingOrderStatus.SHIPPED);
			this.orders.save(order);
		}
	}
}
