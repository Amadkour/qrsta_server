package com.softkour.qrsta_server.repo.public_repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.public_entity.MyNotification;

@Repository
public interface NotificationRepo extends JpaRepository<MyNotification, Long> {

    Page<MyNotification> findAllByUsers_user_id(Long id, Pageable pageable);

    Optional<MyNotification> findAllByIdAndUsers_user_id(Long id, Long userId);

}
