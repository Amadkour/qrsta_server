package com.softkour.qrsta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta.domain.enumeration.CourseType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
// @SuppressWarnings("common-java:DuplicatedBlocks")
public class Course extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
    // "sequenceGenerator")
    // @SequenceGenerator(name = "sequenceGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CourseType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    @JsonIgnoreProperties(value = { "students", "quizes", "course" }, allowSetters = true)
    private Set<Session> sessions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @JsonIgnoreProperties(value = { "parent", "sessions", "courses" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    @JsonIgnoreProperties(value = { "courses" }, allowSetters = true)
    private Set<Schedule> schedules = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseType getType() {
        return this.type;
    }

    public Course type(CourseType type) {
        this.setType(type);
        return this;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public Set<Session> getSessions() {
        return this.sessions;
    }

    public void setSessions(Set<Session> sessions) {
        if (this.sessions != null) {
            this.sessions.forEach(i -> i.setCourse(null));
        }
        if (sessions != null) {
            sessions.forEach(i -> i.setCourse(this));
        }
        this.sessions = sessions;
    }

    public Course sessions(Set<Session> sessions) {
        this.setSessions(sessions);
        return this;
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

    public Set<User> getStudents() {
        return this.students;
    }

//    public void setStudents(Set<User> employees) {
//        if (this.students != null) {
//            this.students.forEach(i -> i.removeCourse(this));
//        }
//        if (employees != null) {
//            employees.forEach(i -> i.addCourse(this));
//        }
//        this.students = employees;
//    }

//    public Course students(Set<User> employees) {
//        this.setStudents(employees);
//        return this;
//    }

    public Course addStudent(User employee) {
        this.students.add(employee);
        employee.getCourses().add(this);
        return this;
    }

    public Course removeStudent(User employee) {
        this.students.remove(employee);
        employee.getCourses().remove(this);
        return this;
    }

    public Set<Schedule> getSchedules() {
        return this.schedules;
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

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", type='" + getType() + "'" +
                "}";
    }
}
