package de.thi.informatik.edi.shop.warehouse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.shop.warehouse.controller.dto.ShipRequest;
import de.thi.informatik.edi.shop.warehouse.controller.dto.ShippingResponse;
import de.thi.informatik.edi.shop.warehouse.services.ShippingService;

@RestController
@RequestMapping("/warehouse/api/v1/shipping")
public class ShippingController {
	private ShippingService warehouse;

	public ShippingController(@Autowired ShippingService warehouse) {
		this.warehouse = warehouse;
	}
	
	@GetMapping
	public List<ShippingResponse> list() {
		List<ShippingResponse> result = new ArrayList<>();
		warehouse.getShippings().forEach(el -> result.add(ShippingResponse.fromShipping(el)));
		return result;
	}
	
	@PostMapping("/{id}/ship")
	public ResponseEntity<?> ship(@PathVariable String id, @RequestBody ShipRequest request) {
		try {
			this.warehouse.doShipping(UUID.fromString(id));
			return ResponseEntity.noContent().build();
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
}
