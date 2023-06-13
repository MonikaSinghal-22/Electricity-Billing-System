package com.monika.Electricity.Billing.System.Service;

import java.util.List;

import com.monika.Electricity.Billing.System.Entity.Customers;

public interface CustomerService {
	
	public Customers createCustomer(Customers customer); 
	
	public List<Customers> getByUserType(String userType);
	
	public Customers getCustomerById(int id);
	
	public Customers getCustomerByUserId(int id);
	
	public List<Integer> getActiveCustomers();

}
