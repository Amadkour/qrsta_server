package com.softkour.qrsta_server.payload.response;

import java.time.Instant;

import com.softkour.qrsta_server.entity.enumeration.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MyNotificationResponse {
    private Long id;
    private Long payLoadId;
    private String title;
    private String description;
    private String imageUrl;
    private NotificationType type;
    private boolean isRead;
    private Instant date;

}
