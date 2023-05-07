package de.thi.informatik.edi.shop.warehouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/warehouse")
public class ViewController {

	@GetMapping("/overview")
	public ModelAndView showWarehouse() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/index.html");
		return modelAndView;
	}

}
