package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softkour.qrsta_server.entity.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
