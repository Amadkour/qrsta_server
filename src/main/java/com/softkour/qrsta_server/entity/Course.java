package com.softkour.qrsta_server.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.payload.response.CourseResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * A Course.
 */
@Entity
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Course extends AbstractAuditingEntity {

    @NotNull
    private String name;
    @NotNull
    // @Size(max = 4)
    private double cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CourseType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "quizes", "courses" }, allowSetters = true)
    private User teacher;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    @JsonIgnoreProperties(value = { "students", "quizzes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @JsonIgnoreProperties(value = { "questions", "courses", "sessions", "students" }, allowSetters = true)
    private Set<Quiz> quizzes = new HashSet<>();

    @OneToMany(mappedBy = "courses")
    private Set<User> students = new HashSet<User>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<User> offers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Schedule> schedules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public void setSessions(Set<Session> sessions) {
        if (this.sessions != null) {
            this.sessions.forEach(i -> i.setCourse(null));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.setCourse(this));
        }
        this.sessions = sessions;
    }

    public Course addSession(Session session) {
        this.sessions.add(session);
        session.setCourse(this);
        return this;
    }

    public Course removeSession(Session session) {
        this.sessions.remove(session);
        session.setCourse(null);
        return this;
    }

    // public void setStudents(Set<User> employees) {
    // if (this.students != null) {
    // this.students.forEach(i -> i.removeCourse(this));
    // }
    // if (employees != null) {
    // employees.forEach(i -> i.addCourse(this));
    // }
    // this.students = employees;
    // }

    public Course students(Set<User> employees) {
        this.setStudents(employees);
        return this;
    }

    public Course addStudent(User employee) {
        this.students.add(employee);
        // employee.getCourse().getStudents().add();
        return this;
    }

    public Course removeStudent(User employee) {
        this.students.remove(employee);
        return this;
    }

    public void setSchedules(Set<Schedule> schedules) {
        if (this.schedules != null) {
            this.schedules.forEach(i -> i.removeCourse(this));
        }
        if (schedules != null) {
            schedules.forEach(i -> i.addCourse(this));
        }
        this.schedules = schedules;
    }

    public Course schedules(Set<Schedule> schedules) {
        this.setSchedules(schedules);
        return this;
    }

    public Course addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        schedule.getCourses().add(this);
        return this;
    }

    public Course removeSchedule(Schedule schedule) {
        this.schedules.remove(schedule);
        schedule.getCourses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return getId() != null && getId().equals(((Course) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public CourseResponse toCourseResponse() {
        return new CourseResponse(this.getId(),
                this.getName(),
                this.getStudents().size(),
                this.getSessions().size(),
                this.getCost(),
                this.getType());
    }
}
