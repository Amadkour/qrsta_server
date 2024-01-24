package com.softkour.qrsta_server.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softkour.qrsta_server.config.GenericResponse;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.quiz.CourseQuiz;
import com.softkour.qrsta_server.entity.quiz.Option;
import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.entity.quiz.Quiz;
import com.softkour.qrsta_server.entity.quiz.SessionQuiz;
import com.softkour.qrsta_server.payload.request.QuizCourseSession;
import com.softkour.qrsta_server.payload.request.QuizCreationRequest;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.OptionService;
import com.softkour.qrsta_server.service.QuestionService;
import com.softkour.qrsta_server.service.QuizService;
import com.softkour.qrsta_server.service.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/quiz/")
@Validated
public class quizController {
    @Autowired
    QuizService quizService;
    @Autowired
    CourseService courseService;
    @Autowired
    SessionService sessionService;
    @Autowired
    QuestionService questionService;
    @Autowired
    OptionService optionService;

    @PostMapping("create")
    public ResponseEntity<GenericResponse<Object>> addQuiz(@RequestBody @Valid QuizCreationRequest request) {
        Quiz quiz = quizService.save(request.toQuiz(courseService, sessionService, optionService, questionService));
        return GenericResponse.success(quiz.toQuizModel());

    }
}
