package com.softkour.qrsta_server.payload.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizCourseSession {
    private Long courseId;
    private List<Long> sessionsId;

}
