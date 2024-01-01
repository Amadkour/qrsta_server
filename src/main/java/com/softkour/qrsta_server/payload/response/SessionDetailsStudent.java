package com.softkour.qrsta_server.payload.response;

import com.softkour.qrsta_server.entity.SessionObject;
import com.softkour.qrsta_server.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;
@Setter
@Getter
public class SessionDetailsStudent {
    private long id;
    private Set<User> students ;
}
