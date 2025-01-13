package com.contabook.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String inicio(HttpServletRequest request, Model model) { 
		
		
		return "index";
		
	}
	
	
}
