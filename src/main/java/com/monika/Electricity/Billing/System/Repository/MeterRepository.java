package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monika.Electricity.Billing.System.Entity.Meters;

public interface MeterRepository extends JpaRepository<Meters,Integer>{

}
