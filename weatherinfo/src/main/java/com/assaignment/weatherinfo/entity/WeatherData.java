package com.assaignment.weatherinfo.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"date", "pincode"})})
public class WeatherData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	private LocalDate date;
	private String temp;
	private double pressure;
	private double humidity;
	private String main;
	private String description;
	@ManyToOne
	@JoinColumn(name="pincode")
	private Pincode pincode;
	
	public WeatherData() {
		super();
	}

	public WeatherData(LocalDate date, String temp, double pressure, double humidity, String main, String description,
			Pincode pincode) {
		super();
		this.date = date;
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		this.main = main;
		this.description = description;
		this.pincode = pincode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double getHumidity() {
		return humidity;
	}

	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Pincode getPincode() {
		return pincode;
	}

	public void setPincode(Pincode pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		return "WeatherData [id=" + id + ", date=" + date + ", temp=" + temp + ", pressure=" + pressure + ", humidity="
				+ humidity + ", main=" + main + ", description=" + description + ", pincode=" + pincode + "]";
	}
	
}
