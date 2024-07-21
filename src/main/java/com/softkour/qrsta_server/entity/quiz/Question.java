package com.softkour.qrsta_server.entity.quiz;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import com.softkour.qrsta_server.payload.request.OptionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuestionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuizCourseSession;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A Question.
 */
@Entity
@Getter
@Setter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question extends AbstractAuditingEntity {

    @NotNull
    @Column(nullable = false)
    private String title;

    @Column()
    private Integer grade;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_question__option", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "option_id"))
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "sessions" }, allowSetters = true)
    private Set<CourseQuiz> coveredSessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "question__quiz", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "quiz_id"))
    @JsonIgnoreProperties(value = { "sessions", "quizzes" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    public void addOption(Option option) {
        this.options.add(option);
    }

    public void removeOption(Option option) {
        this.options.remove(option);
    }

    public void addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
    }

    public void removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
    }

    public QuestionCreationRequest toTeacher(){
        return new QuestionCreationRequest(getId(), getTitle(), getGrade(),
                getOptions().stream()
                        .map(o -> new OptionCreationRequest(o.getTitle(), o.getIsCorrectAnswer()))
                        .collect(Collectors.toSet()),
                getCoveredSessions().stream().map(s -> new QuizCourseSession(
                                s.getSessions().iterator().next().getSession().getCourse(),
                                s.getStartDate(),
                                s.getSessions().stream().map(s2 -> s2.getSession().getId()).toList()))
                        .collect(Collectors
                                .toSet()));
    }
}
