package com.softkour.qrsta_server.entity.public_entity;

import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.MyNotificationResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MyNotification extends AbstractAuditingEntity {

    @NotNull
    private String description;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
    @Column(name = "type")
    private int type;
    @Column(name = "read")
    private boolean read;

    public MyNotificationResponse toNotificationResponse() {
        return new MyNotificationResponse(
                getId(),
                getDescription(),
                getType(),
                isRead(),
                getCreatedDate());
    }
}
