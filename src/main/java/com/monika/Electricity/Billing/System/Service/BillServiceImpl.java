package com.monika.Electricity.Billing.System.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monika.Electricity.Billing.System.Entity.Bill;
import com.monika.Electricity.Billing.System.Repository.BillRepository;

@Service
public class BillServiceImpl implements BillService {
	
	@Autowired
	private BillRepository billRepo;

	@Override
	public Bill createBill(Bill bill) {
		return billRepo.save(bill);
	}

}
