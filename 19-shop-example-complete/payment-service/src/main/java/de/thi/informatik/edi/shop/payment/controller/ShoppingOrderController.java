package de.thi.informatik.edi.shop.payment.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.shop.payment.controller.dto.ShoppingOrderResponse;
import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.services.PaymentService;

@RestController
@RequestMapping("/payment/api/v1/order")
public class ShoppingOrderController {
	private PaymentService payments;
	
	public ShoppingOrderController(@Autowired PaymentService payments) {
		this.payments = payments;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOrder(@PathVariable String id) {
		Payment payment = this.payments.getOrCreateByOrderRef(UUID.fromString(id));
		return ResponseEntity.ok(ShoppingOrderResponse.fromPayment(payment));
	}
}
