package com.softkour.qrsta_server.payload.request;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.firebase.database.annotations.NotNull;
import com.softkour.qrsta_server.entity.enumeration.QuizType;
import com.softkour.qrsta_server.entity.quiz.CourseQuiz;
import com.softkour.qrsta_server.entity.quiz.Quiz;
import com.softkour.qrsta_server.service.OTPService;
import com.softkour.qrsta_server.service.QuestionService;
import com.softkour.qrsta_server.service.QuizService;
import com.softkour.qrsta_server.service.SessionService;
import com.softkour.qrsta_server.service.course.CourseService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuizCreationRequest {
    private Long id;
    @NotNull
    private String timePerMinutes;
    private Instant startDate;

    private String questionsPerStudent;
    @NotNull
    private QuizType type;

    @NotNull
    private Set<QuizCourseSession> courses = new HashSet<>();
    private Set<QuestionCreationRequest> questions = new HashSet<>();

    public Quiz toQuiz(QuizService quizService, CourseService courseService, SessionService sessionService, QuestionService questionService,
                       OTPService otpService) {

        Quiz quiz;
        if (getId() == null) {
            quiz = new Quiz();
            quiz.setCode(otpService.createRandomOneTimeOTP().get());
        } else {
            quiz = quizService.findById(getId());
        }
        quiz.setType(getType());
        quiz.setId(getId());
        quiz.setQuestionsPerStudent(getQuestionsPerStudent());
        quiz.setTimePerMinutes(getTimePerMinutes());
        System.out.println("==============[quiz course]");
        System.out.println(getCourses().stream().map(e->e.getCourseId()).toList());
        getCourses().forEach(e->{
            CourseQuiz courseQuiz = new CourseQuiz();
            courseQuiz.setStartDate(getStartDate());
            courseQuiz.setCourse(courseService.findOne(e.getCourseId()));
            for (Long s : e.getSessionsId()) {
                courseQuiz.addSession(sessionService.findOne(s));
            }
            quiz.addCourses(courseQuiz);
        });
        quiz.setQuestions(new HashSet<>(questionService.findByIds(getQuestions().stream().map(QuestionCreationRequest::getId).toList())));
        return quiz;
    }

}
