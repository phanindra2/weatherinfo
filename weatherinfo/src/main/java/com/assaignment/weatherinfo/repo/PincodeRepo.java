package com.assaignment.weatherinfo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assaignment.weatherinfo.entity.Pincode;

@Repository
public interface PincodeRepo extends JpaRepository<Pincode, String>{

}
