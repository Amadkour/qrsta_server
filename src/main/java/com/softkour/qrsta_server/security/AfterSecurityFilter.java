package com.softkour.qrsta_server.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.public_repo.AppVersionRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AfterSecurityFilter extends OncePerRequestFilter {
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    AppVersionRepo appVersion;

    public HttpServletResponse checkVersion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final String version = request.getHeader("app_version");
        final boolean isIos = request.getHeader("is_ios") == "true";

        if (appVersion.getAppVersionByIosAndAvailableTrue(isIos).getVersion().compareTo(version) != 0) {
            logger.warn("version" + version + ":"
                    + (appVersion.getAppVersionByIosAndAvailableTrue(isIos).getVersion() + ""));
            throw new ClientException("version", "version", 998);
        }
        return response;
    }

    public HttpServletResponse checkPayment(HttpServletResponse response) throws IOException {
        User u = jwtUserDetailsService.findUserByPhoneNumber(JwtRequestFilter.username);
        if (u.getType() == UserType.OBSERVER && u.getParent().getLate() > 0) {
            throw new ClientException("payment", "pay first", 999, u.getParent().getLate() + "");
        }
        return response;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            checkVersion((HttpServletRequest) request, (HttpServletResponse) response);
            chain.doFilter(request, response);

        } catch (ClientException e) {
            Map<String, Object> responseMap = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            response.setStatus(e.code);
            responseMap.put("success", false);
            if (e.code == 998)
                responseMap.put("message", "update app version");
            else {
                responseMap.put("late_by_month", e.additionalString);
                responseMap.put("message", "please pay first");

            }
            response.setHeader("content-type", "application/json");
            String responseMsg = mapper.writeValueAsString(responseMap);
            response.setStatus(998);
            response.getWriter().write(responseMsg);
            return;
        } finally {

        }
        // response = checkPayment((HttpServletResponse) response);

    }
}
