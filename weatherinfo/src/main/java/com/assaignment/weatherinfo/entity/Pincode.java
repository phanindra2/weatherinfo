package com.assaignment.weatherinfo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Pincode {

    @Id
    private String pincode;
    
    private String city;

    private double latitude;
    private double longitude;
       
	
	public Pincode() {
	}

	
	public Pincode(String pincode, String city, double latitude, double longitude) {
		super();
		this.pincode = pincode;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	} 
    
}
