package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.monika.Electricity.Billing.System.Entity.Meters;

public interface MeterRepository extends JpaRepository<Meters,Integer>{

	@Query("From Meters where customer.id = :id")
	Meters getByCustomerId(int id);
}
