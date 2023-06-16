package com.monika.Electricity.Billing.System.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.monika.Electricity.Billing.System.Entity.Meters;

public interface MeterRepository extends JpaRepository<Meters,Integer>{

	@Query("From Meters where customer.id = :id")
	Meters getByCustomerId(int id);
	
	@Query("From Meters where customer.id in :activeCustomerIds")
	List<Meters> getAllByActiveCustomerId(List<Integer> activeCustomerIds);

	Meters getByMeterNo(String meterNo);
}
