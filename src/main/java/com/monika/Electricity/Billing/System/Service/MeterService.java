package com.monika.Electricity.Billing.System.Service;

import java.util.List;

import com.monika.Electricity.Billing.System.Entity.Meters;

public interface MeterService {

	public Meters createMeter(Meters meter);
	
	public Meters getMeterByCustromerId(int id);

	public Meters getMeterById(int meterId);
	
	public List<Meters> getAllEnabledCustomersMeter(List<Integer> activeCustomerIds);
	
}
