package com.softkour.qrsta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta.domain.enumeration.QuizType;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Quiz.
 */
@Entity
@Table(name = "quiz")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quiz extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "create_at")
    private ZonedDateTime createAt;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "questions_per_student")
    private Integer questionsPerStudent;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuizType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_quiz__session", joinColumns = @JoinColumn(name = "quiz_id"), inverseJoinColumns = @JoinColumn(name = "session_id"))
    @JsonIgnoreProperties(value = { "students", "quizes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizzes")
    @JsonIgnoreProperties(value = { "options", "quizzes" }, allowSetters = true)
    private Set<Question> quizzes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quiz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuizId() {
        return this.quizId;
    }

    public Quiz quizId(Long quizId) {
        this.setQuizId(quizId);
        return this;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public Quiz createAt(ZonedDateTime createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Quiz startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getQuestionsPerStudent() {
        return this.questionsPerStudent;
    }

    public Quiz questionsPerStudent(Integer questionsPerStudent) {
        this.setQuestionsPerStudent(questionsPerStudent);
        return this;
    }

    public void setQuestionsPerStudent(Integer questionsPerStudent) {
        this.questionsPerStudent = questionsPerStudent;
    }

    public QuizType getType() {
        return this.type;
    }

    public Quiz type(QuizType type) {
        this.setType(type);
        return this;
    }

    public void setType(QuizType type) {
        this.type = type;
    }

    public Set<Session> getSessions() {
        return this.sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public Quiz sessions(Set<Session> sessions) {
        this.setSessions(sessions);
        return this;
    }

    public Quiz addSession(Session session) {
        this.sessions.add(session);
        return this;
    }

    public Quiz removeSession(Session session) {
        this.sessions.remove(session);
        return this;
    }

    public Set<Question> getQuizzes() {
        return this.quizzes;
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

    public Quiz quizzes(Set<Question> questions) {
        this.setQuizzes(questions);
        return this;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        return getId() != null && getId().equals(((Quiz) o).getId());
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
        return "Quiz{" +
                "id=" + getId() +
                ", quizId=" + getQuizId() +
                ", createAt='" + getCreateAt() + "'" +
                ", startDate='" + getStartDate() + "'" +
                ", questionsPerStudent=" + getQuestionsPerStudent() +
                ", type='" + getType() + "'" +
                "}";
    }
}
