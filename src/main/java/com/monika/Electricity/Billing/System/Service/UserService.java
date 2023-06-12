package com.monika.Electricity.Billing.System.Service;

import java.util.Optional;

import com.monika.Electricity.Billing.System.Entity.Users;

public interface UserService {
	
	public Users createUser(Users user);
	
	public boolean checkUsername(String username);
	
	public Users findUserById(int id);
	
	public Users changeIsAccountLocked(Users user, boolean flag);
		
	
}
