package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monika.Electricity.Billing.System.Entity.PhaseCode;

public interface PhaseCodeRepository extends JpaRepository<PhaseCode, Integer>{

}
