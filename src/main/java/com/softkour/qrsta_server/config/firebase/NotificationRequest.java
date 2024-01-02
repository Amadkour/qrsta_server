package com.softkour.qrsta_server.config.firebase;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationRequest {
    private String title;
    private String body;
    private String topic;
    private String token;
}