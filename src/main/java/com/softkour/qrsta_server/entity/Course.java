package com.softkour.qrsta_server.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.CourseType;
import com.softkour.qrsta_server.payload.response.CourseResponse;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;

import jakarta.persistence.CascadeType;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private Set<StudentCourse> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Offer> offers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses", cascade = CascadeType.ALL)
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

    public Course students(Set<StudentCourse> employees) {
        this.setStudents(employees);
        return this;
    }

    public Course addStudent(StudentCourse employee) {
        if (!this.students.stream().anyMatch(e -> e.getId() == employee.getStudent().getId()))
            this.students.add(employee);
        return this;
    }

    public Course removeStudent(StudentCourse employee) {
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

    public SessionDetailsStudent toSessionDetailsStudent() {

        Set<Session> sessions = this.getSessions();
        // this.getStudents().stream().anyMatch(m -> m.getId() ==
        // e.getStudent().getId())

        return new SessionDetailsStudent(
                getStudents().stream().map(e -> e.getStudent().toStudntInSession(sessions.stream()
                        .map(s -> s.getStudents().stream()
                                .anyMatch(b -> b.getId() == e.getStudent().getId()))
                        .toList(), true, getId())).toList());
    }

    public CourseResponse toCourseResponse() {
        return new CourseResponse(this.getId(),
                this.getName(),
                this.getStudents().size(),
                this.getSessions().size(),
                this.getCost(),
                this.getType(),
                this.getSchedules().stream().map(s -> s.toScheduleResponse()).toList());
    }
}
