package de.thi.informatik.edi.shop.payment.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.thi.informatik.edi.shop.payment.model.Payment;
import de.thi.informatik.edi.shop.payment.services.PaymentService;

@RestController
@RequestMapping("/payment/pay")
public class ViewController {
	
	private PaymentService payments;

	public ViewController(@Autowired PaymentService payments) {
		this.payments = payments;
	}

	@GetMapping("/{orderRef}")
	public ModelAndView showPayment(@PathVariable String orderRef) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/payment.html");
		return modelAndView;
	}

	@GetMapping("/dummy")
	public ResponseEntity<?> showCheckoutDummy() {
		UUID orderRef = UUID.randomUUID();
		Payment payment = payments.getOrCreateByOrderRef(orderRef);
		payments.updatePrice(payment.getId(), 49.99);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.replacePath("/payment/pay/{id}")
				.buildAndExpand(orderRef.toString())
				.toUri();
		return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
	}
}
