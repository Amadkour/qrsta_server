package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.quiz.Option;

/**
 * Spring Data JPA repository for the Option entity.
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
