package com.softkour.qrsta_server.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.UserRepository;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String phone) {
        log.debug("Authenticating {}", phone);

        // if (new P().isValid(phone, null)) {
        com.softkour.qrsta_server.entity.User user = userRepository.findUserByPhoneNumber(phone);
        if (user == null) {
            throw new ClientException("user", "User not found: " + phone);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(),
                user.getPassword(),
                new ArrayList<>());

        // }
    }
}
