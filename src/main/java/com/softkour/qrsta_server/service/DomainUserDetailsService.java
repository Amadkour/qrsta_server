package com.softkour.qrsta_server.service;

import java.util.*;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.repo.UserRepository;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    // @Override
    // @Transactional(readOnly = true)
    // public UserDetails loadUserByUsername(final String phone) {
    // log.debug("Authenticating {}", phone);

    // if (new EmailValidator().isValid(phone, null)) {
    // return userRepository
    // .findOneWithAuthoritiesByEmailIgnoreCase(p)
    // .map(user -> createSpringSecurityUser(login, user))
    // .orElseThrow(() -> new UsernameNotFoundException(
    // "User with email " + login + " was not found in the database"));
    // }

    // // return userRepository.findOneByPhone(phone)
    // // .map(user->createSpringSecurityUser(phone, user)).orElseThrow(null);

    // userRepository
    // .findOneWithAuthoritiesByPhone(phone)
    // .map(user -> createSpringSecurityUser(phone, user))
    // .orElseThrow(() -> new UsernameNotFoundException(
    // "User " + lowercaseLogin + " was not found in the database"));
    // }

    // private org.springframework.security.core.userdetails.User
    // createSpringSecurityUser(String lowercaseLogin,
    // User user) {
    // if (!user.isActivated()) {
    // throw new UserNotActivatedException("User " + lowercaseLogin + " was not
    // activated");
    // }
    // List<SimpleGrantedAuthority> grantedAuthorities = user
    // .getAuthorities()
    // .stream()
    // .map(Authority::getName)
    // .map(SimpleGrantedAuthority::new)
    // .toList();
    // return new
    // org.springframework.security.core.userdetails.User(user.getLogin(),
    // user.getPassword(),
    // grantedAuthorities);
    // }
}
