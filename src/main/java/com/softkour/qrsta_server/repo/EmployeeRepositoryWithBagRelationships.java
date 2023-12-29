package com.softkour.qrsta_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

import com.softkour.qrsta_server.entity.User;

public interface EmployeeRepositoryWithBagRelationships {
    Optional<User> fetchBagRelationships(Optional<User> employee);

    List<User> fetchBagRelationships(List<User> employees);

    Page<User> fetchBagRelationships(Page<User> employees);
}
