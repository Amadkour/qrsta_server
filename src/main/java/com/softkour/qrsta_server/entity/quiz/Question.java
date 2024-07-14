package com.softkour.qrsta_server.entity.quiz;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

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
}
