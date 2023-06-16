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
public class Customers {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	@Nullable
	private String address;
	@Nullable
	private String city;
	@Nullable
	private String state;
	@Nullable
	private String email;
	@Nullable
	private String phone;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="fk_user_id")
	private Users user;
	
	
	@OneToOne(mappedBy = "customer", cascade=CascadeType.ALL)
	private Meters meter;
	
	public Meters getMeter() {
		return meter;
	}

	public void setMeter(Meters meter) {
		this.meter = meter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Customers [Id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", state=" + state
				+ ", email=" + email + ", phone=" + phone + ", user=" + user + "]";
	}
	
}
