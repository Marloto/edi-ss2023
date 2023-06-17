package de.thi.informatik.edi.shop.checkout.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.thi.informatik.edi.shop.checkout.model.ShoppingOrder;
import de.thi.informatik.edi.shop.checkout.services.ShoppingOrderService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/checkout/api/v1/cart")
public class CartController {
	private ShoppingOrderService orders;

	public CartController(@Autowired ShoppingOrderService orders) {
		this.orders = orders;
	}
	
	@PostMapping("/{id}/order")
	public ResponseEntity<?> updateOrder(@PathVariable String id, HttpServletRequest request) {
		ShoppingOrder order = this.orders.createOrderWithCartRef(UUID.fromString(id));
		UUID orderId = order.getId();
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath(ShoppingOrderController.class.getAnnotation(RequestMapping.class).value()[0])
                .path("/{id}")
                .buildAndExpand(orderId.toString())
                .toUri();
		return ResponseEntity.status(HttpStatus.CREATED).location(location).build();
	}

}
