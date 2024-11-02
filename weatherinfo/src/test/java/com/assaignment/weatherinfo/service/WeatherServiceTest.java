package com.assaignment.weatherinfo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.assaignment.weatherinfo.dto.WeatherResponse;
import com.assaignment.weatherinfo.entity.Pincode;
import com.assaignment.weatherinfo.entity.WeatherData;
import com.assaignment.weatherinfo.exceptions.ExceptionFromOpenApi;
import com.assaignment.weatherinfo.repo.PincodeRepo;
import com.assaignment.weatherinfo.repo.WeatherDataRepo;

class WeatherServiceTest{

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PincodeRepo pincodeRepo;

    @Mock
    private WeatherDataRepo weatherDataRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    //Test for Weath data exists in DB
    //if weather exist pincode also exist because we check for pincode first
    @Test
    void getWeatherReport_WeatherDataExists() {
        String pinCode = "123456";
        LocalDate date = LocalDate.of(2023, 11, 1);
        Pincode mockPincode = new Pincode(pinCode, "Test City", 10.0, 20.0);
        WeatherData existingWeatherData = new WeatherData(date, "300", 1015, 70, "Sunny", "Bright and sunny", mockPincode);
        
        when(pincodeRepo.findById(pinCode)).thenReturn(java.util.Optional.of(mockPincode));
        when(weatherDataRepo.findByPincodeAndDate(mockPincode, date)).thenReturn(existingWeatherData);
        
        WeatherData result = weatherService.getWeatherReport(pinCode, date);
        
        assertNotNull(result);
        assertEquals("300", result.getTemp());
        verify(weatherDataRepo, never()).save(any(WeatherData.class)); // Should not save again
    }
    //Test for Weather data not exist in db but pincode exist
    @Test
    void getWeatherReport_PincodeExist() {
        String pinCode = "123456";
        LocalDate date = LocalDate.of(2023, 11, 1);
        Pincode mockPincode = new Pincode(pinCode, "Test City", 10.0, 20.0);
        
        when(pincodeRepo.findById(pinCode)).thenReturn(java.util.Optional.of(mockPincode));
        when(weatherDataRepo.findByPincodeAndDate(mockPincode, date)).thenReturn(null);
        
        WeatherResponse mockResponse = new WeatherResponse();
        WeatherResponse.Data mockData = new WeatherResponse.Data();
        mockData.setTemp("250");
        mockData.setHumidity(60);
        mockData.setPressure(1012);
        mockData.setWeather(Collections.singletonList(new WeatherResponse.Weather()));
        mockResponse.setData(Collections.singletonList(mockData));
        
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(mockResponse);
        
        WeatherData result = weatherService.getWeatherReport(pinCode, date);

        assertNotNull(result);
        assertEquals("250", result.getTemp());
        verify(weatherDataRepo).save(any(WeatherData.class)); 
    }
    
    //Test for pincode not exist in db
    //if pincode not exist then weather also not existed in db
    
    @Test
    void getWeatherReport_PincodeNotExist() {
    	String pincode="123456";
    	LocalDate date=LocalDate.of(2024,10,29);
    	Map<String, Object> mockGeoData = new HashMap<>();
        Map<String, Double> mockCoord = new HashMap<>();
        mockCoord.put("lat", 10.0);
        mockCoord.put("lon", 20.0);
        mockGeoData.put("coord", mockCoord);
        mockGeoData.put("name", "Test City");
    	when(pincodeRepo.findById(pincode)).thenReturn(Optional.empty());
    	when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(mockGeoData);
    	Pincode mockPincode=new Pincode(pincode, mockGeoData.get("name").toString(),(double)((Map)mockGeoData.get("coord")).get("lat"),(double)((Map)mockGeoData.get("coord")).get("lon"));
    	when(weatherDataRepo.findByPincodeAndDate(mockPincode, date)).thenReturn(null);
    	 WeatherResponse mockResponse = new WeatherResponse();
         WeatherResponse.Data mockData = new WeatherResponse.Data();
         mockData.setTemp("250");
         mockData.setHumidity(60);
         mockData.setPressure(1012);
         mockData.setWeather(Collections.singletonList(new WeatherResponse.Weather()));
         mockResponse.setData(Collections.singletonList(mockData));
         
         when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenReturn(mockResponse);
         
         WeatherData result = weatherService.getWeatherReport(pincode, date);
         assertNotNull(result);
         assertEquals("250", result.getTemp());
         verify(weatherDataRepo).save(any(WeatherData.class)); 
    	
    }
    

    @Test
    void getWeatherReport_ExcptionInCallingOpenWeatherGeo() {
        String pinCode = "123456";
        LocalDate date = LocalDate.of(2023, 11, 1);
        
        when(pincodeRepo.findById(pinCode)).thenReturn(java.util.Optional.empty());
        
        ExceptionFromOpenApi exception = assertThrows(ExceptionFromOpenApi.class, () -> {
            weatherService.getWeatherReport(pinCode, date);
        });
        
        assertEquals("Unknown error occurred while contacting open weather API for coordinates ", exception.getMessage());
    }

//this test case is tested and passed for runtime error 
    @Test
    void testGetWeatherReport_FailureFetchingFromOpenApi() {
        String pinCode = "123456";
        LocalDate date = LocalDate.of(2023, 11, 1);
        Pincode mockPincode = new Pincode(pinCode, "Test City", 10.0, 20.0);

        
        when(weatherDataRepo.findByPincodeAndDate(mockPincode, date)).thenReturn(null);
        when(pincodeRepo.findById(pinCode)).thenReturn(java.util.Optional.of(mockPincode));
       
        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class))).thenThrow(new RuntimeException("API error"));

        ExceptionFromOpenApi exception = assertThrows(ExceptionFromOpenApi.class, () -> {
            weatherService.getWeatherReport(pinCode, date);
        });
        
        assertEquals("Unknown error occurred while contacting open weather API for weather report ", exception.getMessage());
      
    }
}
