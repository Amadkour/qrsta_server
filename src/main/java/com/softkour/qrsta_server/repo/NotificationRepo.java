package com.softkour.qrsta_server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.softkour.qrsta_server.entity.public_entity.MyNotification;

public interface NotificationRepo extends JpaRepository<MyNotification, Long> {

    List<NotificationRepo> findAllByUser_id(Long id);

}
