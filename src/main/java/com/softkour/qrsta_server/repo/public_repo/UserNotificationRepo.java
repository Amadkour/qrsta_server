package com.softkour.qrsta_server.repo.public_repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softkour.qrsta_server.entity.public_entity.UserNotification;

@Repository
public interface UserNotificationRepo extends JpaRepository<UserNotification, Long> {

}
