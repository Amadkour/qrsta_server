package com.softkour.qrsta_server.entity.user;

import java.util.HashSet;
import java.util.Set;

import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.enumeration.OrganizationType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * A Employee.
 */
@Entity
// @SuppressWarnings("common-java:DuplicatedBlocks")
@Setter
@Getter
public class Teacher extends AbstractAuditingEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column()
    private OrganizationType organization;

    @Column
    private String paymentaccount;
    /// courses
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "teacher", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    public Set<Course> removeCourse(Course course) {
        courses.remove(course);
        return courses;
    }

    public Set<Course> addCourse(Course course) {
        courses.add(course);
        return courses;
    }

}