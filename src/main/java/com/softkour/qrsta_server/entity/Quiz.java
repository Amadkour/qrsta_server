package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.QuizType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Quiz.
 */
@Entity
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quiz extends AbstractAuditingEntity {

    @Column()
    private ZonedDateTime startDate;

    @Column()
    private Integer questionsPerStudent;

    @Enumerated(EnumType.STRING)
    @Column()
    private QuizType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_quiz__session", joinColumns = @JoinColumn(name = "quiz_id"), inverseJoinColumns = @JoinColumn(name = "session_id"))
    @JsonIgnoreProperties(value = { "students", "quizes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizzes")
    @JsonIgnoreProperties(value = { "options", "quizzes" }, allowSetters = true)
    private Set<Question> quizzes = new HashSet<>();

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public void removeSession(Session session) {
        this.sessions.remove(session);
    }

    public void setQuizzes(Set<Question> questions) {
        if (this.quizzes != null) {
            this.quizzes.forEach(i -> i.removeQuiz(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addQuiz(this));
        }
        this.quizzes = questions;
    }


    public Quiz addQuiz(Question question) {
        this.quizzes.add(question);
        question.getQuizzes().add(this);
        return this;
    }

    public Quiz removeQuiz(Question question) {
        this.quizzes.remove(question);
        question.getQuizzes().remove(this);
        return this;
    }
}
