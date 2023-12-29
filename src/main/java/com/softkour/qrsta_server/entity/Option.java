package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Option extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "option_id")
    private String optionId;

    @Column(name = "name")
    private String name;

    @Column(name = "is_correct_answer")
    private Boolean isCorrectAnswer;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "options")
    @JsonIgnoreProperties(value = { "options", "quizzes" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Option id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionId() {
        return this.optionId;
    }

    public Option optionId(String optionId) {
        this.setOptionId(optionId);
        return this;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getName() {
        return this.name;
    }

    public Option name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsCorrectAnswer() {
        return this.isCorrectAnswer;
    }

    public Option isCorrectAnswer(Boolean isCorrectAnswer) {
        this.setIsCorrectAnswer(isCorrectAnswer);
        return this;
    }

    public void setIsCorrectAnswer(Boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return getId() != null && getId().equals(((Option) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
                "id=" + getId() +
                ", optionId='" + getOptionId() + "'" +
                ", name='" + getName() + "'" +
                ", isCorrectAnswer='" + getIsCorrectAnswer() + "'" +
                "}";
    }
}
