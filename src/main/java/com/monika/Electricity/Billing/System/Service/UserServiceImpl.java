package com.monika.Electricity.Billing.System.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.monika.Electricity.Billing.System.Entity.Users;
import com.monika.Electricity.Billing.System.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Override
	public Users createUser(Users user) {
		if(user.getPassword().equals("")) {
			Users u = userRepo.findById(user.getId()).get();
			user.setPassword(u.getPassword());
		}
		else {
			user.setPassword(passwordEncode.encode(user.getPassword()));
		}
		if(user.getUserType().equals("ROLE_ADMIN")) {
			user.setAccountNonLocked(true);
			user.setEnabled(true);
		}
		return userRepo.save(user);
	}

	@Override
	public boolean checkUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	@Override
	public Users findUserById(int id) {
		return userRepo.findById(id).get();
	}

	@Override
	public Users changeIsAccountLocked(Users user, boolean flag) {
		user.setAccountNonLocked(flag);
		user.setEnabled(flag);
		return userRepo.save(user);
	}

}
