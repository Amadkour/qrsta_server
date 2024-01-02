package com.softkour.qrsta_server.payload.response;

import com.softkour.qrsta_server.entity.SessionObject;
import com.softkour.qrsta_server.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;
@AllArgsConstructor
@Getter
public class SessionDetailsWithoutStudents {
    private Set<SessionObject> objects ;

    private Instant startDate ;

    private Instant endDate ;
}
