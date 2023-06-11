package com.monika.Electricity.Billing.System.Entity;

import org.springframework.lang.Nullable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Meters {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String meterNo;
	@Nullable
	private String meterLocation;
	@Nullable
	private String meterType;
	@Nullable
	private String phaseCode;
	@Nullable
	private String billType;
	@Nullable
	private String days;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="fk_customer_id")
	private Customers customer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public String getMeterLocation() {
		return meterLocation;
	}

	public void setMeterLocation(String meterLocation) {
		this.meterLocation = meterLocation;
	}

	public String getMeterType() {
		return meterType;
	}

	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}

	public String getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public Customers getCustomer() {
		return customer;
	}

	public void setCustomer(Customers customer) {
		this.customer = customer;
	}
	
	
}
