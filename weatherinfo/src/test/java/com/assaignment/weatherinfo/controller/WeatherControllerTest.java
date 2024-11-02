package com.assaignment.weatherinfo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.assaignment.weatherinfo.entity.WeatherData;
import com.assaignment.weatherinfo.exceptions.InvalidRequest;
import com.assaignment.weatherinfo.service.WeatherService;

public class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;

    @Mock
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWeatherReport_Valid() {
        String pinCode = "123456";
        String forDate = "2023-11-01";
        WeatherData mockWeatherData = new WeatherData();
        mockWeatherData.setTemp("256");
        mockWeatherData.setHumidity(60);
        mockWeatherData.setPressure(1012);

        when(weatherService.getWeatherReport(pinCode, LocalDate.parse(forDate))).thenReturn(mockWeatherData);

        ResponseEntity<WeatherData> response = weatherController.getWeatherReport(pinCode, forDate);
        
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("256", response.getBody().getTemp());
    }

    @Test
    void getWeatherReport_InvalidPincode() {
        String invalidPinCode = "12345";
        String forDate = "2023-11-01";

        InvalidRequest exception = assertThrows(InvalidRequest.class, () -> {
            weatherController.getWeatherReport(invalidPinCode, forDate);
        });

        assertEquals("Enter a valid pincode", exception.getMessage());
    }

    @Test
    void getWeatherReport_PinNull() {
        String invalidPinCode = null;
        String forDate = "2023-11-01";

        InvalidRequest exception = assertThrows(InvalidRequest.class, () -> {
            weatherController.getWeatherReport(invalidPinCode, forDate);
        });

        assertEquals("Pincode can't be null", exception.getMessage());
    }

    @Test
    void getWeatherReport_Null() {
        String pinCode = "123456";
        String invalidDate = null;

        InvalidRequest exception = assertThrows(InvalidRequest.class, () -> {
            weatherController.getWeatherReport(pinCode, invalidDate);
        });

        assertEquals("Date can't be empty", exception.getMessage());
    }

    @Test
    void getWeatherReport_InvalidDate() {
        String pinCode = "123456";
        String invalidDate = "invalid-date";

        InvalidRequest exception = assertThrows(InvalidRequest.class, () -> {
            weatherController.getWeatherReport(pinCode, invalidDate);
        });

        assertEquals("Invalid date format. Please use yyyy-MM-dd ", exception.getMessage());
    }
    @Test
    void getWeatherReport_FutureDate() {
        String pinCode = "123456";
        String futureDate = LocalDate.now().plusDays(1).toString();

        InvalidRequest exception = assertThrows(InvalidRequest.class, () -> {
            weatherController.getWeatherReport(pinCode, futureDate);
        });
        
        assertEquals("Date must be today or less than today", exception.getMessage());
//        ResponseEntity<WeatherData> response = weatherController.getWeatherReport(pinCode, futureDate);
//        
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        
//        assertEquals("Date must be today or less than today", response.getBody());
    }

   

}
