package com.softkour.qrsta_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.User;

@Repository
public interface UserRepository extends
        // EmployeeRepositoryWithBagRelationships,
        JpaRepository<User, Long> {
    // default Optional<User> findOneWithEagerRelationships(Long id) {
    // return this.fetchBagRelationships(this.findById(id));
    // }

    // default List<User> findAllWithEagerRelationships() {
    // return this.fetchBagRelationships(this.findAll());
    // }

    // default Page<User> findAllWithEagerRelationships(Pageable pageable) {
    // return this.fetchBagRelationships(this.findAll(pageable));
    // }
}
