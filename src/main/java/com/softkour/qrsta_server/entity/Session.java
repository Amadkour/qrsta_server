package com.softkour.qrsta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Session extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "create_ate")
    private ZonedDateTime createAte;

    @Column(name = "have_quiz")
    private Boolean haveQuiz;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sessions")
    @JsonIgnoreProperties(value = { "parent", "sessions", "courses" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sessions")
    @JsonIgnoreProperties(value = { "sessions", "quizzes" }, allowSetters = true)
    private Set<Quiz> quizes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessions", "students", "schedules" }, allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Session id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreateAte() {
        return this.createAte;
    }

    public Session createAte(ZonedDateTime createAte) {
        this.setCreateAte(createAte);
        return this;
    }

    public void setCreateAte(ZonedDateTime createAte) {
        this.createAte = createAte;
    }

    public Boolean getHaveQuiz() {
        return this.haveQuiz;
    }

    public Session haveQuiz(Boolean haveQuiz) {
        this.setHaveQuiz(haveQuiz);
        return this;
    }

    public void setHaveQuiz(Boolean haveQuiz) {
        this.haveQuiz = haveQuiz;
    }

    public Set<User> getStudents() {
        return this.students;
    }

//    public void setStudents(Set<User> employees) {
//        if (this.students != null) {
//            this.students.forEach(i -> i.removeSession(this));
//        }
//        if (employees != null) {
//            employees.forEach(i -> i.addSession(this));
//        }
//        this.students = employees;
//    }

//    public Session students(Set<User> employees) {
//        this.setStudents(employees);
//        return this;
//    }

    public Session addStudent(User employee) {
        this.students.add(employee);
        employee.getSessions().add(this);
        return this;
    }

    public Session removeStudent(User employee) {
        this.students.remove(employee);
        employee.getSessions().remove(this);
        return this;
    }

    public Set<Quiz> getQuizes() {
        return this.quizes;
    }

    public void setQuizes(Set<Quiz> quizzes) {
        if (this.quizes != null) {
            this.quizes.forEach(i -> i.removeSession(this));
        }
        if (quizzes != null) {
            quizzes.forEach(i -> i.addSession(this));
        }
        this.quizes = quizzes;
    }

    public Session quizes(Set<Quiz> quizzes) {
        this.setQuizes(quizzes);
        return this;
    }

    public Session addQuize(Quiz quiz) {
        this.quizes.add(quiz);
        quiz.getSessions().add(this);
        return this;
    }

    public Session removeQuize(Quiz quiz) {
        this.quizes.remove(quiz);
        quiz.getSessions().remove(this);
        return this;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Session course(Course course) {
        this.setCourse(course);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        return getId() != null && getId().equals(((Session) o).getId());
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
        return "Session{" +
                "id=" + getId() +
                ", createAte='" + getCreateAte() + "'" +
                ", haveQuiz='" + getHaveQuiz() + "'" +
                "}";
    }
}
