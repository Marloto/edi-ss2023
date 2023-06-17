package de.thi.informatik.edi.shop.shopping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/shopping")
public class ViewController {

	@GetMapping("/shop")
	public ModelAndView showShop() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/index.html");
		return modelAndView;
	}

}
