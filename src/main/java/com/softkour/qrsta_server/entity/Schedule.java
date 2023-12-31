package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * A Schedule.
 */
@Entity
@Getter
@Setter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Schedule extends AbstractAuditingEntity {


    @Column()
    private String day;

    @Column()
    private String fromTime;

    @Column()
    private String toTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_schedule__course", joinColumns = @JoinColumn(name = "schedule_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnoreProperties(value = {"sessions", "students", "schedules"}, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }
}
