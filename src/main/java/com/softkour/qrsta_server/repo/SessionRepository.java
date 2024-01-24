package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.course.Session;

/**
 * Spring Data JPA repository for the Session entity.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findAllByCourse_Id(Long courseId);
}
