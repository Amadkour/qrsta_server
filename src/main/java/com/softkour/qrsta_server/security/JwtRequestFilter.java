package com.softkour.qrsta_server.security;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.public_repo.AppVersionRepo;
import com.softkour.qrsta_server.service.AuthService;

import jdk.jshell.execution.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    AppVersionRepo appVersion;
    @Autowired
    AuthService authService;
   public static String username;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        checkVersion(request);

        if (StringUtils.startsWith(requestTokenHeader, "Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                checkPayment(jwtToken);

                if (StringUtils.isNotEmpty(username)
                        && null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(MyUtils.getUserPhone(username));
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext()
                                .setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                logger.error("Unable to fetch JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token is expired");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, response);
    }

    public void checkVersion(HttpServletRequest request) {
        final String version = request.getHeader("version");
        final boolean isIos = request.getHeader("is_ios") == "true";
        if ((appVersion.getAppVersionByIosAndAvailableIsTrue(isIos).getVersion() + "") != version) {
            throw new ClientException("app_version", "please update your version", 998);
        }
    }

    public void checkPayment(String userPhone) {
        User u = authService.getUserByPhoneNumber(userPhone);
        if (u.getType() == UserType.OBSERVER && u.getParent().getLate() > 0) {
            throw new ClientException("payment", (u.getParent().getLate() * -1 + ""), 999);
        }
    }

}