package com.softkour.qrsta.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A Schedule.
 */
@Entity
@Table(name = "schedule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Schedule extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "dat")
    private String dat;

    @Column(name = "from_time")
    private String fromTime;

    @Column(name = "to_time")
    private String toTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_schedule__course", joinColumns = @JoinColumn(name = "schedule_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = { "sessions", "students", "schedules" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Schedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScheduleId() {
        return this.scheduleId;
    }

    public Schedule scheduleId(Long scheduleId) {
        this.setScheduleId(scheduleId);
        return this;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDat() {
        return this.dat;
    }

    public Schedule dat(String dat) {
        this.setDat(dat);
        return this;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public String getFromTime() {
        return this.fromTime;
    }

    public Schedule fromTime(String fromTime) {
        this.setFromTime(fromTime);
        return this;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return this.toTime;
    }

    public Schedule toTime(String toTime) {
        this.setToTime(toTime);
        return this;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Schedule courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Schedule addCourse(Course course) {
        this.courses.add(course);
        return this;
    }

    public Schedule removeCourse(Course course) {
        this.courses.remove(course);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schedule)) {
            return false;
        }
        return getId() != null && getId().equals(((Schedule) o).getId());
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
        return "Schedule{" +
                "id=" + getId() +
                ", scheduleId=" + getScheduleId() +
                ", dat='" + getDat() + "'" +
                ", fromTime='" + getFromTime() + "'" +
                ", toTime='" + getToTime() + "'" +
                "}";
    }
}
