package com.monika.Electricity.Billing.System.Service;

import com.monika.Electricity.Billing.System.Entity.Users;

public interface UserService {
	
	public Users createUser(Users user);
	
	public boolean checkUsername(String username);
		
	
}
