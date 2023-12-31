package com.softkour.qrsta_server.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
public class SessionDateAndStudentGrade {
    private Instant startDate;
    private  Instant endDate;

    private int attendance_count;
    private int grade;

}