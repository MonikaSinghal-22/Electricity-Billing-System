package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monika.Electricity.Billing.System.Entity.MeterLocation;

public interface MeterLocationRepository extends JpaRepository<MeterLocation, Integer>{

	MeterLocation findByNameContainingIgnoreCase(String meterLocation);

}
