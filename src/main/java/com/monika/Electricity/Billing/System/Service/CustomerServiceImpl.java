package com.monika.Electricity.Billing.System.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monika.Electricity.Billing.System.Entity.Customers;
import com.monika.Electricity.Billing.System.Repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	
	@Override
	public Customers createCustomer(Customers customer) {
		return customerRepo.save(customer); 
	}

	@Override
	public List<Customers> getByUserType(String userType) {
		return customerRepo.getByUserType(userType);
	}

	@Override
	public Customers getCustomerById(int id) {
		return customerRepo.findById(id).get();
	}

}
