package com.softkour.qrsta_server.payload.request;
import com.softkour.qrsta_server.entity.enumeration.QuizType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class QuizCreationRequest {
    private Long id;
    private Instant startDate;
    private int timePerSeconds;
    private int questionsPerStudent;
    private QuizType type;
    private Set<QuizCourseSession> coures = new HashSet<>();
    private Set<QuestionCreationRequest> questions = new HashSet<>();

}
