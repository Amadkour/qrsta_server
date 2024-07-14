
package com.softkour.qrsta_server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.user.Parent;

/**
 * Spring Data JPA repository for the Option entity.
 */
@Repository
public interface ParentReppo extends JpaRepository<Parent, Long> {
}
