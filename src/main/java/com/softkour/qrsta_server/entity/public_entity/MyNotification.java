package com.softkour.qrsta_server.entity.public_entity;

import java.util.HashSet;
import java.util.Set;

import com.softkour.qrsta_server.entity.enumeration.NotificationType;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.payload.response.MyNotificationResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class MyNotification extends AbstractAuditingEntity {

    @Column
    private Long payLoadId;

    @NotNull
    @Column
    private String description;
    @NotNull
    @Column
    private String title;
    @Column
    private String imageUrl;
    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    private Set<UserNotification> users = new HashSet<>();
    @Enumerated(EnumType.STRING)
    @Column()

    private NotificationType type;

    public MyNotificationResponse toNotificationResponse(Boolean read) {

        return new MyNotificationResponse(
                getId(),
                getPayLoadId(),
                getTitle(),
                getDescription(),
                getImageUrl(),
                getType(),
                getUsers().iterator()
                        .next().isRead(),
                getCreatedDate());
    }

}
