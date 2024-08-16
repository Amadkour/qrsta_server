package com.softkour.qrsta_server.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SessionDateAndStudentGrade {
    private Long reminderPerMinutes;
    private Long perioudPerMinutes;
    private long sessionId;
    private String label;
    private Integer absence_count;
    private Integer expected_count;
    private Double grade;
    private Boolean finished;
    private Boolean isAttendance;

}
