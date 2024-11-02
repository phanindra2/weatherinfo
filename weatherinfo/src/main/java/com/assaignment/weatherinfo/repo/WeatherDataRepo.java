package com.assaignment.weatherinfo.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assaignment.weatherinfo.entity.Pincode;
import com.assaignment.weatherinfo.entity.WeatherData;

@Repository
public interface WeatherDataRepo extends JpaRepository<WeatherData, Integer> {

	WeatherData findByPincodeAndDate(Pincode zip, LocalDate date);

}
