package com.softkour.qrsta_server.payload.request;

import com.google.firebase.database.annotations.NotNull;
import com.softkour.qrsta_server.entity.enumeration.QuizType;
import com.softkour.qrsta_server.entity.quiz.CourseQuiz;
import com.softkour.qrsta_server.entity.quiz.Quiz;
import com.softkour.qrsta_server.entity.quiz.SessionQuiz;
import com.softkour.qrsta_server.service.OTPService;
import com.softkour.qrsta_server.service.QuestionService;
import com.softkour.qrsta_server.service.QuizService;
import com.softkour.qrsta_server.service.SessionService;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
public class QuizCorrectionRequest {
    @NotNull
    private Long quizId;
    @NotNull
    private List<Long> questionsId;
    @NotNull
    private List<List<String>> answers;

}
