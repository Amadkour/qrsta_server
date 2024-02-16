package com.softkour.qrsta_server.entity.public_entity;

import com.softkour.qrsta_server.entity.course.SessionObject;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.StudentSchedualResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StudentSchedual extends AbstractAuditingEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SessionObject object;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
    @Column
    private boolean read;
    @Column
    private boolean done;

    public StudentSchedualResponse toStudentSchedualResponse() {
        return new StudentSchedualResponse(
                getId(),
                getObject().getTitle(),
                isRead(),
                isDone(),
                getCreatedDate());
    }

}
