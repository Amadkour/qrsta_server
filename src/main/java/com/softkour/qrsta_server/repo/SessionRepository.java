package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.Session;

import java.util.List;

/**
 * Spring Data JPA repository for the Session entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findAllByCourse_Id(Long courseId);
}
