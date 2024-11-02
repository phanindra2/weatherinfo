package com.assaignment.weatherinfo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assaignment.weatherinfo.entity.WeatherData;
import com.assaignment.weatherinfo.exceptions.FutureDateException;
import com.assaignment.weatherinfo.exceptions.InvalidRequest;
import com.assaignment.weatherinfo.service.WeatherService;

@RestController
@RequestMapping("/api/weather/")
public class WeatherController {
	
	@Autowired
	private WeatherService weatherService;
	
	@GetMapping
	public ResponseEntity<WeatherData> getWeatherReport(@RequestParam String pinCode,
			@RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd")String forDate) {
		if(pinCode==null || pinCode.isBlank() || pinCode.isEmpty()) {
			throw new InvalidRequest("Pincode can't be null");		
		}
		if(pinCode.strip().length()!=6) {
			throw new InvalidRequest("Enter a valid pincode");
		}
		if(forDate==null || forDate.isBlank() || forDate.isEmpty()) {
			throw new InvalidRequest("Date can't be empty");
		}
		
		LocalDate requestedDate;
        try {
            requestedDate = LocalDate.parse(forDate);
        } catch (Exception e) {
            throw new InvalidRequest("Invalid date format. Please use yyyy-MM-dd ");
        }
        if (requestedDate.isAfter(LocalDate.now())) {
        	throw new InvalidRequest("Date must be today or less than today");
        }
        pinCode=pinCode.strip();
        WeatherData wd=weatherService.getWeatherReport(pinCode,requestedDate);
		return ResponseEntity.ok().body(wd);
		}

}
