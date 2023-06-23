package com.monika.Electricity.Billing.System.Service;

import java.util.List;

import com.monika.Electricity.Billing.System.Entity.Bill;

public interface BillService {

	Bill createBill(Bill bill);
	
	List<Bill> getAllBills();
	
	Bill getBillById(int id);

	boolean deleteBill(int id);

}
