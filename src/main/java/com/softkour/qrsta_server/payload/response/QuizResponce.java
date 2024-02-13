package com.softkour.qrsta_server.payload.response;

import java.time.Instant;
import java.util.List;

import com.softkour.qrsta_server.entity.enumeration.QuizType;
import com.softkour.qrsta_server.entity.quiz.Quiz;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizResponce {

    private Long id;
    private List<String> courses;
    private QuizType type;
    private Instant startDate;
    private String timePerMinutes;
    private String code;
    private int studentCount;
    private int questionCount;
    private int points;

}
