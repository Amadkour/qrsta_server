package com.softkour.qrsta_server.entity.assiociation_entity;

import java.io.Serializable;

import com.softkour.qrsta_server.entity.Course;
import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.entity.embedded_pk.StudentCoursePK;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity
@Data
public class StudentCourse implements Serializable {
    public StudentCourse(User u, Course c) {
        id = new StudentCoursePK(u.getId(), c.getId());
        course = c;
        student = u;
    }

    @EmbeddedId
    private StudentCoursePK id;

    @ManyToOne
    @MapsId("student_id") // This is the name of attr in EmployerDeliveryAgentPK class
    @JoinColumn(name = "user_id")
    private User student;

    @ManyToOne
    @MapsId("course_id")
    @JoinColumn(name = "course_id")
    private Course course;

    @Column
    private int late = 0;
}