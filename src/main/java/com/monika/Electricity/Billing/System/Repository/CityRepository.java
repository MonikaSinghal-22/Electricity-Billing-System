package com.monika.Electricity.Billing.System.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.monika.Electricity.Billing.System.Entity.City;

public interface CityRepository extends JpaRepository<City, Integer>{

	@Query("From City where state.id = :id")
	List<City> getByStateId(int id);
	
	City findByNameContainingIgnoreCase(String name);
}
