package de.thi.informatik.edi.shop.shopping.controller;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.thi.informatik.edi.shop.shopping.controller.dto.AddArticleRequest;
import de.thi.informatik.edi.shop.shopping.controller.dto.CartResponse;
import de.thi.informatik.edi.shop.shopping.controller.dto.CreateCartRequest;
import de.thi.informatik.edi.shop.shopping.controller.dto.FinishCartRequest;
import de.thi.informatik.edi.shop.shopping.model.Cart;
import de.thi.informatik.edi.shop.shopping.services.CartService;

@RestController
@RequestMapping("/shopping/api/v1/cart")
public class CartController {
	
	private CartService carts;

	public CartController(@Autowired CartService carts) {
		this.carts = carts;
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CartResponse> getCart(@PathVariable String id) {
		Optional<Cart> cart = this.carts.getCart(UUID.fromString(id));
		if(cart.isPresent()) {
			return new ResponseEntity<CartResponse>(CartResponse.fromCart(cart.get()), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/{id}/article")
	public ResponseEntity<?> addArticleToCart(@PathVariable String id, @RequestBody AddArticleRequest req) {
		try {			
			this.carts.addArticle(UUID.fromString(id),  UUID.fromString(req.getArticle()));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}/article/{article}")
	public ResponseEntity<?> deleteArticleFromCart(@PathVariable String id, @PathVariable String article) {
		try {			
			this.carts.deleteArticle(UUID.fromString(id),  UUID.fromString(article));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/{id}/finish")
	public ResponseEntity<?> finishCart(@PathVariable String id, @RequestBody FinishCartRequest req) {
		try {			
			this.carts.cartFinished(UUID.fromString(id));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(Exception e) {			
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> create(@RequestBody CreateCartRequest req) {
		UUID id = carts.createCart();
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id.toString())
                .toUri();
		return ResponseEntity.status(HttpStatus.CREATED).location(location).build();
	}
}
