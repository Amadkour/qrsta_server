package com.softkour.qrsta_server.payload.request;
import com.softkour.qrsta_server.entity.enumeration.QuizType;
import lombok.Data;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class QuizCreationRequest {
    private Instant startDate;
    private int questionsPerStudent;
    private QuizType type;
    private Set<Long> sessionIds = new HashSet<>();
    private Set<Long> courseIds = new HashSet<>();
    private Set<QuestionCreationRequest> questions = new HashSet<>();

}
