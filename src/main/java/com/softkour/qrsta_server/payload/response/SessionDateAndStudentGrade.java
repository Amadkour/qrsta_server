package com.softkour.qrsta_server.payload.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SessionDateAndStudentGrade {
    private Instant startDate;
    private Instant endDate;
    private long sessionId;
    private String label;
    private int absence_count;
    private int expected_count;
    private double grade;
    private boolean isAttendance;

}
