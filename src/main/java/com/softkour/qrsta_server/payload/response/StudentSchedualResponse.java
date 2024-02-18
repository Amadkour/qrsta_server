package com.softkour.qrsta_server.payload.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class StudentSchedualResponse {
    private Long id;
    private Instant dueDate;
    private int weight;
    private String session;
    private String course;
    private boolean isRead;
    private boolean done;
    private Instant date;

}
