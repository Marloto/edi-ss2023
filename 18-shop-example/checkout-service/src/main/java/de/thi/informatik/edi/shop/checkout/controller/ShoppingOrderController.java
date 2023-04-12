package de.thi.informatik.edi.shop.checkout.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.shop.checkout.controller.dto.ModifyOrderRequest;
import de.thi.informatik.edi.shop.checkout.controller.dto.ShoppingOrderResponse;
import de.thi.informatik.edi.shop.checkout.model.ShoppingOrder;
import de.thi.informatik.edi.shop.checkout.services.ShoppingOrderService;

@RestController
@RequestMapping("/checkout/api/v1/order")
public class ShoppingOrderController {
	private ShoppingOrderService orders;

	public ShoppingOrderController(@Autowired ShoppingOrderService orders) {
		this.orders = orders;
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody ModifyOrderRequest request) {
		this.orders.updateOrder(UUID.fromString(id), 
				request.getFirstName(), request.getLastName(), request.getStreet(), request.getZipCode(), request.getCity());
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ShoppingOrderResponse> get(@PathVariable String id) {
		try {			
			ShoppingOrder order = this.orders.find(UUID.fromString(id));
			return ResponseEntity.ok(ShoppingOrderResponse.fromOrder(order));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/{id}/place-order")
	public ResponseEntity<?> placeOrder(@PathVariable String id) {
		this.orders.placeOrder(UUID.fromString(id));
		
		return ResponseEntity.noContent().build();
	}
}
