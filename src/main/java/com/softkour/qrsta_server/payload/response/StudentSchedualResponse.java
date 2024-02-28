package com.softkour.qrsta_server.payload.response;

import java.time.Instant;

import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.payload.request.QuestionCreationRequest;

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
    private SessionDateAndStudentGrade session;
    private String course;
    private boolean isRead;
    private boolean done;
    private Instant date;
    private QuestionCreationRequest question;

}
