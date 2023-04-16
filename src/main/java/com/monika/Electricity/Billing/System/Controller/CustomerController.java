package com.monika.Electricity.Billing.System.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Repository.UserRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private UserRepository userRepo; 
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute
	private void userDtls(Model m,Principal p) {
		String username = p.getName();
		Users user = userRepo.findByUsername(username);
		m.addAttribute("user",user);
	}
	
	@GetMapping("/dashboard")
	public String loadChangePassword() {
		return "customer/dashboard";
	}
	
	@GetMapping("/changePassword")
	public String changePassword() {
		return "changePassword";
	}
	
	@PostMapping("/updatePassword")
	public String changePassword(Principal p, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttrs) {
		String username = p.getName();
		Users loginUser = userRepo.findByUsername(username);
		boolean oldPasswordMatch = passwordEncoder.matches(oldPassword, loginUser.getPassword());
		if(oldPasswordMatch) {
			loginUser.setPassword(passwordEncoder.encode(newPassword));
			Users user = userRepo.save(loginUser);
			if(user != null) {
				redirectAttrs.addFlashAttribute("successMessage", "Password Changed Successfully!");
			}
			else {
				redirectAttrs.addFlashAttribute("failureMessage", "Error occured, please try again later.");
			}
			
		}
		else {
			redirectAttrs.addFlashAttribute("failureMessage", "Incorrect old password.");
		}
		return "redirect:/customer/changePassword";
	}
	
}
