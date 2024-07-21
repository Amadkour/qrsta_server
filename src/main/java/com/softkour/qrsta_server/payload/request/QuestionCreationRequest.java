package com.softkour.qrsta_server.payload.request;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.softkour.qrsta_server.entity.quiz.Option;
import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreationRequest {
    private Long id;
    private String title;
    private int grade;
    private Set<OptionCreationRequest> options = new HashSet<>();
    private Set<QuizCourseSession> coveredSessions = new HashSet<>();

    public Question toQuestion(SessionService sessionService) {
        Question question = new Question();
        question.setGrade(getGrade());
        question.setTitle(getTitle());
        question.setOptions(getOptions().stream().map(OptionCreationRequest::toOption).collect(Collectors.toSet()));
        question.setCoveredSessions(getCoveredSessions().stream().map(e -> e.toCourseQuiz(sessionService)).collect(Collectors.toSet()));
        return question;
    }

}
