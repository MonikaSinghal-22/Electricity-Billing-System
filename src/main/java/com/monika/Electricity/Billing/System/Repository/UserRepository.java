package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monika.Electricity.Billing.System.Entity.Users;

public interface UserRepository extends JpaRepository<Users,Integer>{
	
	public boolean existsByUsername(String username);
	
	public Users findByUsername(String username);

}
