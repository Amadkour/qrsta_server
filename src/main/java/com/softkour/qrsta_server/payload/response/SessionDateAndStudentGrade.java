package com.softkour.qrsta_server.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
@AllArgsConstructor
public class SessionDateAndStudentGrade {
    private Instant startDate;
    private  Instant endDate;
    private long sessionIs;
    private int attendance_count;
    private double grade;

}
