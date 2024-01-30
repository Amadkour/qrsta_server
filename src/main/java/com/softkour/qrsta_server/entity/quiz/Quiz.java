package com.softkour.qrsta_server.entity.quiz;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.QuizType;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.payload.request.OptionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuestionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuizCourseSession;
import com.softkour.qrsta_server.payload.request.QuizCreationRequest;
import com.softkour.qrsta_server.payload.response.QuizResponce;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Setter
@Getter
@Slf4j
public class Quiz extends AbstractAuditingEntity {

    @Column()
    private Instant startDate;

    @Column()
    private String questionsPerStudent;

    @Column()
    private String timePerMinutes;

    @Enumerated(EnumType.STRING)
    @Column()
    private QuizType type;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "sessions" }, allowSetters = true)
    private Set<CourseQuiz> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizzes", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "options", "quizzes" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.removeQuiz(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addQuiz(this));
        }
        this.questions = questions;
    }

    public Quiz addQuestion(Question question) {
        this.questions.add(question);
        question.getQuizzes().add(this);
        return this;
    }

    public Quiz removeQuestion(Question question) {
        this.questions.remove(question);
        question.getQuizzes().remove(this);
        return this;
    }

    public QuizResponce toQuizModel() {
        QuizResponce quizResponce = new QuizResponce();
        quizResponce.setCourses(getCourses().stream().map(e -> e.getCourse().getName()).toList());
        if (getType() == QuizType.ONLINE) {
            quizResponce.setStartDate(getStartDate());
        } else {
            log.warn(String.valueOf(getCourses().iterator().next().getSessions().size()));
            if (getType() == QuizType.BEGIN) {
                quizResponce.setStartDate(
                        getCourses().iterator().next().getSessions().stream()
                                .toList().get(0)
                                .getSession().getStartDate());
            } else if (getType() == QuizType.END) {
                quizResponce.setStartDate(
                        getCourses().iterator().next().getSessions().stream()
                                .toList().get(0)
                                .getSession().getEndDate().minusSeconds(Integer.parseInt(getTimePerMinutes()) * 60));
            }
        }
        quizResponce.setPoints(getQuestions().stream().mapToInt(e -> e.getGrade()).sum());
        quizResponce.setId(getId());
        quizResponce.setQuestionCount(getQuestions().size());
        quizResponce.setStudentCount(
                getCourses().stream().mapToInt(e -> e.getCourse().getStudents().size()).sum());
        quizResponce.setType(getType());
        quizResponce.setTimePerMinutes(getTimePerMinutes());
        return quizResponce;

    }

    public List<QuestionCreationRequest> toStudentQuiz() {
        return getQuestions().stream()
                .map(e -> new QuestionCreationRequest(e.getId(), e.getTitle(), e.getGrade(), e.getOptions().stream()
                        .map(o -> new OptionCreationRequest(o.getTitle(), false)).collect(Collectors.toSet())))
                .toList();

    }

    public QuizCreationRequest toTeacherQuiz() {
        QuizCreationRequest quiz = new QuizCreationRequest();
        quiz.setCourses(getCourses().stream().map(e -> new QuizCourseSession(
                e.getCourse().getId(), e.getSessions().stream().map(s -> s.getSession().getId()).toList()))
                .collect(Collectors.toSet()));
        quiz.setQuestions(
                getQuestions().stream().map(e -> new QuestionCreationRequest(e.getId(), e.getTitle(), e.getGrade(),
                        e.getOptions().stream()
                                .map(o -> new OptionCreationRequest(o.getTitle(), o.getIsCorrectAnswer()))
                                .collect(Collectors.toSet())))
                        .collect(Collectors.toSet()));
        return quiz;
    }
}
