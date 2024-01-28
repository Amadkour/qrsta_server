package com.softkour.qrsta_server.payload.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizCourseSession {
    private Long courseId;
    private List<Long> sessionsId;

}
