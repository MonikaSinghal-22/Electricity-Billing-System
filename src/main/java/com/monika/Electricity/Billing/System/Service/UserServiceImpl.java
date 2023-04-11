package com.monika.Electricity.Billing.System.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public Users createUser(Users user) {
		return userRepo.save(user);
	}

	@Override
	public boolean checkUsername(String username) {
		return userRepo.existsByUsername(username);
	}

}
