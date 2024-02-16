package com.softkour.qrsta_server.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.softkour.qrsta_server.service.OTPService;
import com.softkour.qrsta_server.service.OptionService;
import com.softkour.qrsta_server.service.QuestionService;
import com.softkour.qrsta_server.service.QuizService;
import com.softkour.qrsta_server.service.SessionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/quiz/")
@Validated
@Slf4j
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
    @Autowired
    OTPService otpService;

    @PostMapping("create")
    public ResponseEntity<GenericResponse<Object>> addQuiz(@RequestBody @Valid QuizCreationRequest request) {
        Quiz quiz = quizService
                .save(request.toQuiz(quizService, courseService, sessionService, optionService, questionService,
                        otpService));
        log.warn(String.valueOf(quiz.getCourses().iterator().next().getCourse().getId()));
        return GenericResponse.success(quiz.toQuizModel());

    }

    @GetMapping("delete")
    public ResponseEntity<GenericResponse<Object>> addQuiz(@RequestHeader("id") Long id) {
        quizService.delete(id);
        return GenericResponse.successWithMessageOnly("delete_successfully");

    }
    @GetMapping("all")
    public ResponseEntity<GenericResponse<Object>> allQuiz(
            @RequestHeader(required = false, name = "child_phone") String childPhone) {
        return GenericResponse.success(quizService.findAll(childPhone).stream().map(e -> e.toQuizModel()));
    }

    @GetMapping("course_score")
    public ResponseEntity<GenericResponse<Object>> courseScore() {
        return GenericResponse.success(quizService.findAll(null).stream().map(e -> e.toQuizModel()));
    }

    @GetMapping("quiz_profile_for_teacher")
    public ResponseEntity<GenericResponse<Object>> getQuiz(@RequestHeader("quiz_id") Long quizId) {
        return GenericResponse.success(quizService.findById(quizId).toTeacherQuiz());
    }

    @GetMapping("quiz_profile_for_student")
    public ResponseEntity<GenericResponse<Object>> getStudentQuiz(@RequestHeader("quiz_id") Long quizId) {
        return GenericResponse.success(quizService.findById(quizId).toStudentQuiz());
    }

    @GetMapping("correct_quiz")
    public ResponseEntity<GenericResponse<Object>> correct(@RequestHeader("answer") List<List<String>> answers,
            @RequestHeader("quiz_id") Long quizId) {
        return GenericResponse.successWithMessageOnly(quizService.correct(answers, quizId));
    }
}
