package com.monika.Electricity.Billing.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.monika.Electricity.Billing.System.Repository.UserRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class ElectricityBillingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectricityBillingSystemApplication.class, args);
	}

}
