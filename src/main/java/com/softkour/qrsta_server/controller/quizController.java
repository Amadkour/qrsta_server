package com.softkour.qrsta_server.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.entity.quiz.StudentQuiz;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.payload.request.QuestionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuizCorrectionRequest;
import com.softkour.qrsta_server.repo.quiz.StudentQuizRepo;
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
import com.softkour.qrsta_server.entity.quiz.Quiz;
import com.softkour.qrsta_server.payload.request.QuizCreationRequest;
import com.softkour.qrsta_server.service.OTPService;
import com.softkour.qrsta_server.service.OptionService;
import com.softkour.qrsta_server.service.QuestionService;
import com.softkour.qrsta_server.service.QuizService;
import com.softkour.qrsta_server.service.SessionService;
import com.softkour.qrsta_server.service.course.CourseService;

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
    @Autowired
    OTPService otpService;

    @PostMapping("create")
    public ResponseEntity<GenericResponse<Object>> addQuiz(@RequestBody @Valid QuizCreationRequest request) {

        Quiz quiz = quizService
                .save(request.toQuiz(quizService,courseService, sessionService,questionService,
                        otpService));
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
        return GenericResponse.success(quizService.findAll(childPhone).stream().map(Quiz::toQuizModel));
    }

    @GetMapping("course_score")
    public ResponseEntity<GenericResponse<Object>> courseScore() {
        return GenericResponse.success(quizService.findAll(null).stream().map(Quiz::toQuizModel));
    }

    @GetMapping("quiz_profile_for_teacher")
    public ResponseEntity<GenericResponse<Object>> getQuiz(@RequestHeader("quiz_id") Long quizId) {
        return GenericResponse.success(quizService.findById(quizId).toTeacherQuiz());
    }

    @GetMapping("quiz_profile_for_student")
    public ResponseEntity<GenericResponse<Object>> getStudentQuiz(@RequestHeader("quiz_id") Long quizId) {
        return GenericResponse.success(quizService.findById(quizId).toStudentQuiz());
    }

    @PostMapping("correct_quiz")
    public ResponseEntity<GenericResponse<Object>> correct(@RequestBody QuizCorrectionRequest quizCorrectionRequest) {
        return GenericResponse.successWithMessageOnly(quizService.correct(quizCorrectionRequest));
    }
    @GetMapping("update_student_degree")
    public ResponseEntity<GenericResponse<Object>> updateDegree(@RequestHeader Long quizId,@RequestHeader Long studentId,@RequestHeader double newDegree ) {
        StudentQuiz studentQuiz=quizService.updateDegree(quizId,studentId,newDegree);
        return GenericResponse.successWithMessageOnly("update "+ Arrays.stream(studentQuiz.getStudent().getUser().getName().split(" ")).iterator().next()+"'s degree to be"+studentQuiz.getGrade());
    }
    @GetMapping("quiz_students")
    public ResponseEntity<GenericResponse<Object>> quizStudents(@RequestHeader Long quizId,@RequestHeader String courseName) {
        return GenericResponse.success(quizService.findByQuizIdAndCourseName(quizId,courseName).getCourses().stream().iterator().next().getStudents().stream().map(e->e.getStudent().toAbstractUserWithDegree(e.getGrade(),e.getAnswers())).toList());
    }
}
