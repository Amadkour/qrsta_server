package com.softkour.qrsta_server.payload.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MyNotificationResponse {
    private Long id;
    private String discription;
    private int type;
    private boolean isRead;
    private Instant date;

}
