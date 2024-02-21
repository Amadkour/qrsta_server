package com.softkour.qrsta_server.entity.course;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.GroupAssignmentResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class GroupAssignment extends AbstractAuditingEntity {
    @Column
    private List<Double> degrees;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private List<String> mediaUrls;
    @Column
    private Instant dueDate;
    @Column
    private boolean active = false;
    @Column
    private boolean finished = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Assignment assignment;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user__assignment", joinColumns = @JoinColumn(name = "assignment_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Set<User> students = new HashSet<>();

    public GroupAssignmentResponse toGroupResponse() {
        return new GroupAssignmentResponse(
                getId(),
                getStudents().stream().map(e -> e.toAbstractUser()).toList(),
                getDegrees(),
                getMediaUrls());
    }

    public void addStudent(User user) {
        students.add(user);
    }
}
