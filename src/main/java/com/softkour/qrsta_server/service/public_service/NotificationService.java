package com.softkour.qrsta_server.service.public_service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.config.firebase.FCMInitializer;
import com.softkour.qrsta_server.config.firebase.NotificationRequest;
import com.softkour.qrsta_server.entity.enumeration.NotificationType;
import com.softkour.qrsta_server.entity.public_entity.MyNotification;
import com.softkour.qrsta_server.entity.public_entity.UserNotification;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.public_repo.NotificationRepo;
import com.softkour.qrsta_server.repo.public_repo.UserNotificationRepo;

@Service
public class NotificationService {
    @Autowired
    NotificationRepo notificationRepo;
    @Autowired
    UserNotificationRepo userNotificationRepo;
    @Autowired
    private FCMInitializer fcmService;

    public Page<MyNotification> getNotifications(Long id, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        PageRequest.of(0, 5, Sort.by("createdDate").descending());
        return notificationRepo.findAllByUsers_user_id(id, pageable);
    }

    public void addNotification(NotificationType type, String desc, Long itemId, Set<User> users) {
        MyNotification notification = new MyNotification();
        Set<UserNotification> savedUser = new HashSet<>();
        users.forEach(u -> {
            if (u.getFcmToken() != null) {
                System.out.println(u.getFcmToken());
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setToken(u.getFcmToken());
                notificationRequest.setBody(desc);
                notificationRequest.setTitle(type.name());
                notificationRequest.setTopic(type.name());
                fcmService.sendPushNotificationService(notificationRequest);
            }
            /// local notifications
            UserNotification userNotification = new UserNotification();
            userNotification.setRead(false);
            userNotification.setUser(u);
            savedUser.add(userNotificationRepo.save(userNotification));

        });
        System.out.println("=============[add notification]==============");

        System.out.println(savedUser.stream().map(e -> e.getUser().getId()).toList());
        notification.setDescription(desc);
        notification.setTitle(type.name());
        notification.setType(type);
        notification.setPayLoadId(itemId);
        notification.setUsers(savedUser);
        notificationRepo.save(notification);
    }

    public void deleteNotification(Long id) {
        MyNotification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new ClientException("notification", "notification not found"));
        notificationRepo.delete(notification);
    }

    public void readNotification(Long id, Long userId) {
        MyNotification notification = notificationRepo.findAllByIdAndUsers_user_id(id, userId)
                .orElseThrow(() -> new ClientException("notification", "notification not found"));

        UserNotification user = notification.getUsers().iterator().next();
        user.setRead(true);
        user = userNotificationRepo.save(user);
        Set<UserNotification> users = new HashSet<>();
        users.add(user);
        notification.setUsers(users);
        notificationRepo.save(notification);
    }

}
