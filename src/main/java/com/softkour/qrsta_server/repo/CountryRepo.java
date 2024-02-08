package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.public_entity.Country;

public interface CountryRepo extends JpaRepository<Country, Long> {

}
