package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.SessionObject;

@Repository
public interface SessionObjectRepo extends JpaRepository<SessionObject, Long> {

}
