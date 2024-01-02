package com.softkour.qrsta_server.config;

import com.softkour.qrsta_server.entity.Option;
import com.softkour.qrsta_server.entity.Question;
import com.softkour.qrsta_server.entity.Quiz;
import com.softkour.qrsta_server.payload.request.QuizCreationRequest;
import com.softkour.qrsta_server.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz/")
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
    public ResponseEntity<GenericResponse<String>> addQuiz(@RequestBody @Valid QuizCreationRequest request){
        try{
            Quiz quiz = new Quiz();
            quiz.setType(request.getType());
            quiz.setSessions(request.getSessionIds().stream().map(sessionService::findOne).collect(Collectors.toSet()));
            quiz.setCourses(request.getSessionIds().stream().map(courseService::findOne).collect(Collectors.toSet()));
            ///questions
            quiz.setQuestions(request.getQuestions().stream().map(q -> {
                Question question = new Question();
                ///options
                question.setOptions(q.getOptions().stream().map(o -> {
                    Option option = new Option();
                    option.setTitle(o.getTitle());
                    option.setIsCorrectAnswer(o.getIsCorrectAnswer());
                    return optionService.save(option);
                }).collect(Collectors.toSet()));
                question.setTitle(q.getTitle());
                question.setGrade(q.getGrade());
                return questionService.save(question);
            }).collect(Collectors.toSet()));
            quizService.save(quiz);
            return GenericResponse.successWithMessageOnly("save quiz successfully");
        }catch(Exception e){
           return GenericResponse.error(e.getMessage());
        }
    }
}
