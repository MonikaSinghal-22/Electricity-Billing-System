package com.monika.Electricity.Billing.System.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monika.Electricity.Billing.System.Entity.BillType;

public interface BillTypeRepository extends JpaRepository<BillType, Integer>{

}
