package com.softkour.qrsta_server.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.repo.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) {
        com.softkour.qrsta_server.entity.user.User user = userRepository.findUserByPhoneNumber(phone);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(user.getType().name()));
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getLogoutTimes()).concat("+").concat(user.getPhoneNumber()), user.getPassword(),
                authorityList);
    }
}