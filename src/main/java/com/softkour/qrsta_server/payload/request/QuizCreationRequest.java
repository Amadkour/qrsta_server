package com.softkour.qrsta_server.payload.request;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.firebase.database.annotations.NotNull;
import com.softkour.qrsta_server.entity.enumeration.QuizType;
import com.softkour.qrsta_server.entity.quiz.CourseQuiz;
import com.softkour.qrsta_server.entity.quiz.Option;
import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.entity.quiz.Quiz;
import com.softkour.qrsta_server.entity.quiz.SessionQuiz;
import com.softkour.qrsta_server.service.CourseService;
import com.softkour.qrsta_server.service.OptionService;
import com.softkour.qrsta_server.service.QuestionService;
import com.softkour.qrsta_server.service.QuizService;
import com.softkour.qrsta_server.service.SessionService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class QuizCreationRequest {
    private Long id;
    private Instant startDate;
    @NotNull
    private String timePerMinutes;

    private String questionsPerStudent;
    @NotNull
    private QuizType type;
    @NotNull
    private Set<QuizCourseSession> courses = new HashSet<>();
    private Set<QuestionCreationRequest> questions = new HashSet<>();

    public Quiz toQuiz(QuizService quizService, CourseService courseService, SessionService sessionService,
            OptionService optionService,
            QuestionService questionService) {

        Quiz quiz;
        if (getId() == null) {
            quiz = new Quiz();
        } else {
            quiz = quizService.findById(getId());
        }
        quiz.setType(getType());
        quiz.setId(getId());
        quiz.setQuestionsPerStudent(getQuestionsPerStudent());
        quiz.setTimePerMinutes(getTimePerMinutes());
        quiz.setStartDate(getStartDate());
        quiz.setCourses(
                getCourses().stream().map(new Function<QuizCourseSession, CourseQuiz>() {
                    @Override
                    public CourseQuiz apply(QuizCourseSession e) {
                        CourseQuiz courseQuiz = new CourseQuiz();
                        courseQuiz.setCourse(courseService.findOne(e.getCourseId()));
                        log.warn(String.valueOf(getCourses().iterator().next().getCourseId()));

                        e.getSessionsId().stream()
                                .forEach(s -> courseQuiz.addSession(new SessionQuiz(sessionService.findOne(s))));

                        return courseQuiz;
                    }
                })
                        .collect(Collectors.toSet()));
        /// questions
        quiz.setQuestions(getQuestions().stream().map(q -> {
            Question question = new Question();
            /// options
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
        return quiz;
    }

}
