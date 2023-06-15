package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monika.Electricity.Billing.System.Entity.State;

public interface StateRepository extends JpaRepository<State, Integer>{

	State findByNameContainingIgnoreCase(String name);
	
}
