package com.monika.Electricity.Billing.System.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monika.Electricity.Billing.System.Entity.Meters;
import com.monika.Electricity.Billing.System.Repository.MeterRepository;

@Service
public class MeterServiceImpl implements MeterService {

	@Autowired
	private MeterRepository meterRepo;
	
	@Override
	public Meters createMeter(Meters meter) {
		return meterRepo.save(meter);
	}

	@Override
	public Meters getMeterByCustromerId(int id) {
		return meterRepo.getByCustomerId(id);
	}

	@Override
	public Meters getMeterById(int meterId) {
		return meterRepo.findById(meterId).get();
	}

	@Override
	public List<Meters> getAllEnabledCustomersMeter(List<Integer> activeCustomerIds) {
		return meterRepo.getAllByActiveCustomerId(activeCustomerIds);
	}

	
}
