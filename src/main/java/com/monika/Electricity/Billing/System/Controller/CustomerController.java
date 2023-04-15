package com.monika.Electricity.Billing.System.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Repository.UserRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private UserRepository userRepo; 
	
	@ModelAttribute
	private void userDtls(Model m,Principal p) {
		String username = p.getName();
		Users user = userRepo.findByUsername(username);
		m.addAttribute("user",user);
	}
	
	@GetMapping("/")
	public String dashboard() {
		return "customer/dashboard";
	}
}
