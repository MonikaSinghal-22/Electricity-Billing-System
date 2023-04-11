package com.monika.Electricity.Billing.System.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
	public String createUser(@ModelAttribute Users user) {
		//System.out.println(user.getMeterNo()+ " "+user.getName()+" "+ user.getPassword()+" "+user.getUsername()+" "+user.getUserType());
		String username = user.getUsername();
		boolean usernameExistFlag = userService.checkUsername(username); 
		if(usernameExistFlag) {
			System.out.println("Username Already Exist");
		}
		else {
			Users userDetails = userService.createUser(user);
			if(userDetails != null) {
				System.out.println("Registered Successfully");
			}
			else {
				System.out.println("Error occured, please try again later.");
			}
		}
		return "index";
	}
}
