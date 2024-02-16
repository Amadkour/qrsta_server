package com.softkour.qrsta_server.repo;

import java.time.Instant;
import java.util.List;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.course.Session;

/**
 * Spring Data JPA repository for the Session entity.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findAllByCourse_Id(Long courseId);

    List<Session> findAllByCourse_IdAndStartDateAfter(Long courseId, Instant date);

    List<Session> findAllByCourse_IdAndStartDateBefore(Long courseId, Instant date);
}
