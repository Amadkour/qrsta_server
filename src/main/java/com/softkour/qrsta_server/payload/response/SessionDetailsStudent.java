package com.softkour.qrsta_server.payload.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SessionDetailsStudent {
    private Set<AbstractUser> students;
}
