package com.assaignment.weatherinfo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherResponse {
	@JsonProperty("lat")
	private double latitude;
	@JsonProperty("lon")
	private double longitude;
	@JsonProperty("data")
	private List<Data> data;
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
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public static class Data{
		@JsonProperty
		private String temp;
		@JsonProperty
		private double pressure;
		@JsonProperty
		private double humidity;
		@JsonProperty("weather")
		private List<Weather> weather;
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
		public List<Weather> getWeather() {
			return weather;
		}
		public void setWeather(List<Weather> weather) {
			this.weather = weather;
		}
		@Override
		public String toString() {
			return "Data [temp=" + temp + ", pressure=" + pressure + ", humidity=" + humidity + ", weather=" + weather
					+ "]";
		}
		
		
	}
	public static class Weather{
		@JsonProperty
		private String main;
		@JsonProperty
		private String description;
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
		@Override
		public String toString() {
			return "Weather [main=" + main + ", description=" + description + "]";
		}
	
	}
	@Override
	public String toString() {
		return "WeatherResponse [latitude=" + latitude + ", longitude=" + longitude + ", data=" + data + "]";
	}
	
	
	

	
	
}
