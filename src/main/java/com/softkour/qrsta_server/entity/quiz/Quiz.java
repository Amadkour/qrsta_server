package com.softkour.qrsta_server.entity.quiz;

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
import com.softkour.qrsta_server.payload.response.QuizResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Quiz extends AbstractAuditingEntity {


    @Column()
    private String questionsPerStudent;

    @Column()
    private String code;

    @Column()
    private String timePerMinutes;

    @Enumerated(EnumType.STRING)
    @Column()
    private QuizType type;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"sessions"}, allowSetters = true)
    private Set<CourseQuiz> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizzes", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"options", "quizzes"}, allowSetters = true)
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

    public QuizResponse toQuizModel() {
        QuizResponse quizResponse = new QuizResponse();
        if (getType() == QuizType.ONLINE) {
            quizResponse.setStartDate(getCourses().iterator().next().getStartDate());
        } else {
            if (getType() == QuizType.BEGIN) {
                quizResponse.setStartDate(
                        getCourses().iterator().next().getSessions().stream()
                                .toList().get(0)
                                .getSession().getStartDate());
            } else if (getType() == QuizType.END) {
                quizResponse.setStartDate(
                        getCourses().iterator().next().getSessions().stream()
                                .toList().get(0)
                                .getSession().getEndDate().minusSeconds(Integer.parseInt(getTimePerMinutes()) * 60L));
            }
        }
        quizResponse.setPoints(getQuestions().stream().mapToInt(Question::getGrade).sum());
        quizResponse.setId(getId());
        quizResponse.setQuestionCount(getQuestionsPerStudent());
        quizResponse.setStudentCount(
                getCourses().stream().mapToInt(e -> e.getSessions().iterator().next().getSession().getCourse().getStudents().size()).sum());
        quizResponse.setType(getType());
        quizResponse.setTimePerMinutes(getTimePerMinutes());
        quizResponse.setCode(getCode());
        return quizResponse;

    }

    public List<QuestionCreationRequest> toStudentQuiz() {
        return getQuestions().stream()
                .map(e -> new QuestionCreationRequest(
                        e.getId(),
                        e.getTitle(),
                        e.getGrade(),
                        e.getOptions().stream()
                                .map(o -> new OptionCreationRequest(o.getTitle(), false)).collect(Collectors.toSet()),
                        e.getCoveredSessions().stream().map(s -> new QuizCourseSession(
                                        s.getSessions().iterator().next().getSession().getCourse(),
                                        s.getStartDate(),
                                        s.getSessions().stream().map(s2 -> s2.getSession().getId()).toList()))
                                .collect(Collectors
                                        .toSet())))
                .toList();

    }

    public QuizCreationRequest toTeacherQuiz() {
        QuizCreationRequest quiz = new QuizCreationRequest();
        quiz.setCourses(getCourses().stream().map(e -> new QuizCourseSession(
                        e.getSessions().iterator().next().getSession().getCourse(),
                        e.getStartDate(),
                        e.getSessions().stream().map(s -> s.getSession().getId()).toList()))
                .collect(Collectors.toSet()));
        quiz.setQuestions(
                getQuestions().stream().map(Question::toTeacher)
                        .collect(Collectors.toSet()));
        return quiz;
    }
}
