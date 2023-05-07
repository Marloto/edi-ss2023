package de.thi.informatik.edi.shop.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thi.informatik.edi.shop.shopping.controller.dto.StockResponse;
import de.thi.informatik.edi.shop.shopping.services.StockService;

@RestController
@RequestMapping("/shopping/api/v1/stock")
public class StockController {
	private StockService stocks;

	public StockController(@Autowired StockService stocks) {
		this.stocks = stocks;
	}
	
	@GetMapping
	public List<StockResponse> listAll() {
		return StreamSupport.stream(this.stocks.list().spliterator(), false)
			.map(StockResponse::fromStock)
			.collect(Collectors.toList());
		
	}
}
