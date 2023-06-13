package com.monika.Electricity.Billing.System.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.monika.Electricity.Billing.System.Entity.Customers;

public interface CustomerRepository extends JpaRepository<Customers, Integer>{

	@Query("From Customers where user.userType = :userType and user.enabled = 0000000000000001")
	List<Customers> getByUserType(String userType);
	
	@Query("From Customers where user.id = :id")
	Customers getByUserId(int id);
}
