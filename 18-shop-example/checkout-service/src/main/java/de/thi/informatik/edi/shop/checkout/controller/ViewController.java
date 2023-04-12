package de.thi.informatik.edi.shop.checkout.controller;

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

import de.thi.informatik.edi.shop.checkout.services.ShoppingOrderService;

@RestController
@RequestMapping("/checkout")
public class ViewController {
	private ShoppingOrderService orders;

	public ViewController(@Autowired ShoppingOrderService orders) {
		this.orders = orders;
	}

	@GetMapping("/view/{id}")
	public ModelAndView showOrder(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/order.html");
		return modelAndView;
	}

	@GetMapping("/do/{cartRef}")
	public ModelAndView showCheckout(@PathVariable String cartRef) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/checkout.html");
		return modelAndView;
	}

	@GetMapping("/do/dummy")
	public ResponseEntity<?> showCheckoutDummy() {
		UUID cartRef = UUID.randomUUID();
		this.orders.createOrderWithCartRef(cartRef);
		this.orders.addItemToOrderByCartRef(cartRef, UUID.randomUUID(), "Test 1", 12.34, 1);
		this.orders.addItemToOrderByCartRef(cartRef, UUID.randomUUID(), "Test 1", 4.99, 2);
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/checkout/do/{id}")
                .buildAndExpand(cartRef.toString())
                .toUri();
		return ResponseEntity.status(HttpStatus.FOUND).location(location).build();
	}
}
