package com.monika.Electricity.Billing.System.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@PostMapping("/createUser")
	public String createUser(@ModelAttribute Users user, RedirectAttributes redirectAttrs) {
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
		return "redirect:/";
	}
	
	@PostMapping("/signin")
	public String signin(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttrs) {
		if(username.equals("admin") && password.equals("admin")) {
			redirectAttrs.addFlashAttribute("successMessage", "Success");
		}
		else {
			redirectAttrs.addFlashAttribute("failureMessage", "Invalid username or password! Try Again.");
		}
		return "redirect:/";
	}
}
