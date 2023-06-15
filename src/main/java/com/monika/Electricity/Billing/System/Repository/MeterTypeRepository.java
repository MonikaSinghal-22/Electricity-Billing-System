package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monika.Electricity.Billing.System.Entity.MeterType;

public interface MeterTypeRepository extends JpaRepository<MeterType, Integer>{

	MeterType findByNameContainingIgnoreCase(String meterType);

}
