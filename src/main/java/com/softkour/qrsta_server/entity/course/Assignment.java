package com.softkour.qrsta_server.entity.course;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.AssignmentType;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.payload.response.AssignmentResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Assignment extends AbstractAuditingEntity {
    @Column
    private String title;
    @Column(nullable = true)
    private String description;
    @Column
    private Instant dueDate;
    @Column
    private int max_count;
    @Column
    private int min_count;
    @Column
    private AssignmentType type;
    @Column
    private boolean active = false;
    @Column
    public boolean showForAll = false;
    @Column
    public boolean mustApprove = false;
    @Column
    private boolean finished = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Course course;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "GroupAssignment_id")
    @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
    private Set<GroupAssignment> groups = new HashSet<>();

    public AssignmentResponse toAssignmentResponse() {
        return new AssignmentResponse(
                getId(),
                getTitle(),
                getDescription(),
                getDueDate(),
                !isFinished(),
                getType());
    }

    public void addGroup(GroupAssignment group) {
        groups.add(group);
    }

    public void remove(GroupAssignment group) {
        groups.removeIf((e) -> e.getId() == group.getId());
    }

    public void update(GroupAssignment group) {
        remove(group);
        addGroup(group);
    }

}
