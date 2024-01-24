package com.softkour.qrsta_server.entity.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * A Option.
 */
@Getter
@Setter
@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Option extends AbstractAuditingEntity {
    @Column()
    private String title;

    @Column()
    private Boolean isCorrectAnswer;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "options")
    @JsonIgnoreProperties(value = { "options", "quizzes" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.removeOption(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addOption(this));
        }
        this.questions = questions;
    }

    public Option questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Option addQuestion(Question question) {
        this.questions.add(question);
        question.getOptions().add(this);
        return this;
    }

    public Option removeQuestion(Question question) {
        this.questions.remove(question);
        question.getOptions().remove(this);
        return this;
    }

}
