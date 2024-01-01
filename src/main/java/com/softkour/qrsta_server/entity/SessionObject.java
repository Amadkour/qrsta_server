package com.softkour.qrsta_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.SessionObjectType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class SessionObject extends AbstractAuditingEntity {
    @Column
    private String title;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SessionObject> subItems=new HashSet<>();

    @ManyToOne(fetch =FetchType.LAZY)
    private Session session;

    @Column
    @Enumerated(EnumType.STRING)
    private SessionObjectType type;


    public void addSubItem(SessionObject sessionObject){
        subItems.add(sessionObject);
    }

    public void removeSubItem(SessionObject sessionObject){
        subItems.remove(sessionObject);
    }
}
