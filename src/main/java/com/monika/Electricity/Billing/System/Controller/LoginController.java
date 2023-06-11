package com.monika.Electricity.Billing.System.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.monika.Electricity.Billing.System.Entity.Customers;
import com.monika.Electricity.Billing.System.Entity.Meters;
import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Repository.UserRepository;
import com.monika.Electricity.Billing.System.Service.CustomerService;
import com.monika.Electricity.Billing.System.Service.MeterService;
import com.monika.Electricity.Billing.System.Service.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	@GetMapping("/login")
	public String login() {
		return "index";
	}
	
	@PostMapping("/createUser")
	public String createUser(@ModelAttribute Users user,@ModelAttribute Customers customer,@ModelAttribute Meters meter,  RedirectAttributes redirectAttrs) {
		
		String username = user.getUsername();
		boolean usernameExistFlag = userService.checkUsername(username); 
		if(usernameExistFlag) {
			redirectAttrs.addFlashAttribute("failureMessage", "Username Already Exist.");
		}
		else {
			Users userDetails = userService.createUser(user);
			if(userDetails != null) {
				String userType = userDetails.getUserType();
				if(userType.equals("ROLE_CUSTOMER")) {
					customer.setUser(userDetails);
					Customers customerDetails = customerService.createCustomer(customer);
					meter.setCustomer(customerDetails);
					meterService.createMeter(meter);
				}
				redirectAttrs.addFlashAttribute("successMessage", "Account Created! Login to Continue.");
			}
			else {
				redirectAttrs.addFlashAttribute("failureMessage", "Error occured, please try again later.");
			}
		}
		return "redirect:/login";
	}
	
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttrs) {
		Users user = userRepo.findByUsername(username);
		if(user != null) {
			if(password.equals(confirmPassword)) {
				user.setPassword(passwordEncoder.encode(password));
				Users userDtls = userRepo.save(user);
				if(userDtls != null) {
					redirectAttrs.addFlashAttribute("successMessage", "Password has been reset successfully! Login to Continue.");
				}
				else {
					redirectAttrs.addFlashAttribute("failureMessage", "Error occured, please try again later.");
				}
			}
			else {
				redirectAttrs.addFlashAttribute("failureMessage", "Password & Confirm Password Mismatch");
			}
			
		}
		else {
			redirectAttrs.addFlashAttribute("failureMessage", "Invalid Username");
		}
		return "redirect:/login";
	}
}
