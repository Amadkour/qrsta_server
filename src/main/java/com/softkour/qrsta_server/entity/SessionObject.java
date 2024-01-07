package com.softkour.qrsta_server.entity;

import java.util.HashSet;
import java.util.Set;

import com.softkour.qrsta_server.entity.enumeration.SessionObjectType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class SessionObject extends AbstractAuditingEntity {
    @Column
    private String title;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SessionObject> subItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @Column
    @Enumerated(EnumType.STRING)
    private SessionObjectType type;

    public void addSubItem(SessionObject sessionObject) {
        subItems.add(sessionObject);
    }

    public void removeSubItem(SessionObject sessionObject) {
        subItems.remove(sessionObject);
    }
}
