package com.softkour.qrsta_server.payload.response;

import com.softkour.qrsta_server.entity.SessionObject;
import com.softkour.qrsta_server.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class SessionDetailsStudent {
    private Set<UserLogo> students;
}
