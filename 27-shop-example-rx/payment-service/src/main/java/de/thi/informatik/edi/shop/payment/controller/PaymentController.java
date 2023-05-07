package de.thi.informatik.edi.shop.payment.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.shop.payment.controller.dto.PayRequest;
import de.thi.informatik.edi.shop.payment.controller.dto.PaymentResponse;
import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.services.PaymentService;

@RestController
@RequestMapping("/payment/api/v1/payment")
public class PaymentController {
	private PaymentService payments;

	public PaymentController(@Autowired PaymentService payments) {
		this.payments = payments;
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getPayment(@PathVariable String id) {
		Payment payment = this.payments.findById(UUID.fromString(id));
		return ResponseEntity.ok(PaymentResponse.fromPayment(payment));
	}
	
	@PostMapping("/{id}/pay")
	public ResponseEntity<?> pay(@PathVariable String id, @RequestBody PayRequest request) {
		this.payments.pay(UUID.fromString(id));
		return ResponseEntity.noContent().build();
	}
}
