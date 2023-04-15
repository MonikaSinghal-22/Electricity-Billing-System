package com.monika.Electricity.Billing.System.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String login() {
		return "index";
	}
	
	@PostMapping("/createUser")
	public String createUser(@ModelAttribute Users user, RedirectAttributes redirectAttrs) {
		System.out.println(user.getUserType());
		
		String username = user.getUsername();
		boolean usernameExistFlag = userService.checkUsername(username); 
		if(usernameExistFlag) {
			redirectAttrs.addFlashAttribute("failureMessage", "Username Already Exist.");
		}
		else {
			Users userDetails = userService.createUser(user);
			if(userDetails != null) {
				redirectAttrs.addFlashAttribute("successMessage", "Account Created! Login to Continue.");
			}
			else {
				redirectAttrs.addFlashAttribute("failureMessage", "Error occured, please try again later.");
			}
		}
		return "redirect:/login";
	}
}
