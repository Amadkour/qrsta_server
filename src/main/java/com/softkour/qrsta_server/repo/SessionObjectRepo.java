package com.softkour.qrsta_server.repo;

import com.softkour.qrsta_server.entity.SessionObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionObjectRepo extends JpaRepository<SessionObject,Long> {


}
