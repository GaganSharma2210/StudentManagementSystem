package com.manya.studentManagementSystem.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class StudentEntity extends BaseEntity{
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false, length = 10, unique = true)
	private Long contactNumber;
	
	@Column(nullable = false, length = 100)
	private String address;
	
	@Column(nullable = false, length = 10)
	private Long pincode;
	
	@Column(nullable = false, unique = true)
	private String encryptedPassword;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getPincode() {
		return pincode;
	}

	public void setPincode(Long pincode) {
		this.pincode = pincode;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	
	
	
	

}
