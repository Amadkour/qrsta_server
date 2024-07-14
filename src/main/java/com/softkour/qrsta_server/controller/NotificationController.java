package com.softkour.qrsta_server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.public_entity.MyNotification;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.MyNotificationResponse;
import com.softkour.qrsta_server.service.AuthService;
import com.softkour.qrsta_server.service.public_service.NotificationService;

@RestController
@RequestMapping("/api/notifications/")
public class NotificationController {

        @Autowired
        NotificationService notificationService;
        @Autowired
        AuthService userService;

        @GetMapping("get_all")
        public ResponseEntity<GenericResponse<Object>> notifications(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int pageSize) {
                // try {
                System.out.println("===================11111=============");
                User user = MyUtils.getCurrentUserSession(userService);
                System.out.println("===================22222=============");
                List<MyNotification> notifications = notificationService.getNotifications(user.getId(), page, pageSize)
                                .getContent();
                System.out.println(notifications);

                List<MyNotificationResponse> notifications2 = notificationService
                                .getNotifications(user.getId(), page, pageSize)
                                .getContent().stream()
                                .map(e -> e.toNotificationResponse(false))
                                .toList();
                System.out.println(notifications2);
                return GenericResponse
                                .success(notifications2);
                // } catch (Exception e) {
                // return GenericResponse
                // .successWithMessageOnly("uauthorization");
                // }
        }

        @GetMapping("delete")
        public ResponseEntity<GenericResponse<Object>> deleteNotifications(
                        @RequestParam Long notificationId) {
                notificationService.deleteNotification(notificationId);
                return GenericResponse.successWithMessageOnly("deleted successfully");
        }

        @GetMapping("read")
        public ResponseEntity<GenericResponse<Object>> readNotifications(
                        @RequestParam Long notificationId) {
                User user = MyUtils.getCurrentUserSession(userService);
                notificationService.readNotification(notificationId, user.getId());
                return GenericResponse.successWithMessageOnly("deleted successfully");
        }
}
