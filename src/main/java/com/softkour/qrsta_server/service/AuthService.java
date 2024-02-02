package com.softkour.qrsta_server.service;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.user.Parent;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.payload.request.AcceptRequest;
import com.softkour.qrsta_server.payload.request.LoginRequest;
import com.softkour.qrsta_server.payload.request.ParentRegisterRequest;
import com.softkour.qrsta_server.payload.request.RegisterationRequest;
import com.softkour.qrsta_server.payload.request.UpdateUserRequest;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;
import com.softkour.qrsta_server.repo.UserRepository;
import com.softkour.qrsta_server.security.JwtTokenUtil;
import com.softkour.qrsta_server.security.JwtUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {
    @Autowired
    UserRepository authorRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUserDetailsService userDetailsService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    OTPService otpService;

    public User register(RegisterationRequest request) {
        if (authorRepository.existsByPhoneNumber(request.getPhone())) {
            throw new ClientException("phone_number", "phone number already exists");
        } else if (authorRepository.existsByNationalId(request.getNationalId())) {
            throw new ClientException("national_id", "national_id number already exists");
        }
        return authorRepository.save(request.toUser(otpService));
    }

    public User update(UpdateUserRequest request) {
        User user = authorRepository.findUserByPhoneNumber(request.getPhoneNumber());
        User updateUser = request.toUser(user);
        return authorRepository.save(updateUser);
    }

    public UserLoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword()));
            User user = authorRepository.findUserByPhoneNumber(request.getPhone());

            if (user == null) {
                throw new ClientException("user", "user does not exist");
            } else if (!user.isActive()) {
                throw new ClientException("activation", "user does not Active");
            } else if (auth.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getPhone());
                String token = jwtTokenUtil.generateToken(userDetails);
                user.setLoginMacAddress(request.getMacAddress());
                UserLoginResponse userLoginResponse = user.toUserLoginResponse();
                userLoginResponse.setToken(token);
                return userLoginResponse;
            } else {
                throw new ClientException("user", "Invalid Credentials");
            }
        } catch (DisabledException e) {
            throw new ClientException("invalid_Credentials", "User is disabled");
        } catch (BadCredentialsException e) {
            throw new ClientException("invalid_credentials", "Invalid Credentials");
        }
    }

    public String createParent(ParentRegisterRequest request) {
        /// link
        if (request.getNationalId() == null) {
            if (!authorRepository.existsByPhoneNumber(request.getPhone())) {
                throw new ClientException("phone_number", "phone number are invaild");
            } else {
                User u = authorRepository.findUserByPhoneNumber(request.getPhone());
                if (u.getType() == UserType.STUDENT) {
                    throw new ClientException("phone_number", "linked account must be a parent");
                }
                Supplier<String> otp = otpService.createRandomOneTimeOTP();
                u.setOtp(otp.get());
                u.setExpireOTPDateTime(Instant.now());
                authorRepository.save(u);
                return u.getOtp();
            }
        } else {
            /// create
            if (authorRepository.existsByPhoneNumber(request.getPhone())) {
                throw new ClientException("phone_number", "phone number already exists");
            }
            User user = authorRepository.save(request.toUser(otpService));
            return user.getOtp();

        }
    }

    public String acceptToChangeDevice(List<AcceptRequest> requests) {
        for (AcceptRequest acceptRequest : requests) {
            User user = authorRepository.findById((Long.parseLong(acceptRequest.getId())))
                    .orElseThrow(() -> new ClientException("switch_device", "user not fount: " + acceptRequest
                            .getId()));
            user.setRegisterMacAddress(user.getLoginMacAddress());
            authorRepository.save(user);
        }
        return "accept to change device successfully";

    }

    public String cancleRequest(List<AcceptRequest> requests) {
        for (AcceptRequest acceptRequest : requests) {
            User user = authorRepository.findById((Long.parseLong(acceptRequest.getId())))
                    .orElseThrow(() -> new ClientException("switch_device", "user not fount: " + acceptRequest
                            .getId()));
            user.setNeedToReplace(false);
            authorRepository.save(user);
        }
        return "cancle request successfully";

    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return authorRepository.findUserByPhoneNumber(phoneNumber);
    }

    public List<User> getNeedToReplaceUsers(Long teacherId) {
        return authorRepository.findAllUserByNeedToReplaceAndCourses_course_teacherId(false, teacherId);
    }

    public User save(User user) {
        return authorRepository.save(user);
    }

    public List<User> getChildren(AuthService authService) {
        return authorRepository.findAllChildrenByParent_id(
                MyUtils.getCurrentUserSession(authService).getId());
    }

    public User getParent(AuthService authService) {
        return ((Student) MyUtils.getCurrentUserSession(authService)).getParent();
    }

    public User deleteChild(Long childId) {
        Student u = (Student) authorRepository.findById(childId)
                .orElseThrow(() -> new ClientException("user", "parent not found"));
        u.setParent(null);
        return authorRepository.save(u);
    }

    public User addChild(AuthService authService, Long childId) {
        Student child = (Student) authorRepository.findById(childId)
                .orElseThrow(() -> new ClientException("user", "child_not_found"));
        child.setParent((Parent) MyUtils.getCurrentUserSession(authService));
        return authorRepository.save(child);
    }

    public User verifyParentUser(String otp, String parentPhone, Student currentUser) {
        User parentUser = getUserByPhoneNumber(parentPhone);
        if (parentUser.getOtp().equalsIgnoreCase(otp) && !parentUser.getExpireOTPDateTime().isBefore(Instant.now())) {
            parentUser.setActive(true);
            parentUser = save(parentUser);
            currentUser.setParent((Parent) parentUser);
            save(currentUser);
            return parentUser;
        } else {
            throw new ClientException("user", "Invalid Credentials");
        }
    }

    public User getUserById(Long userId) {
        return authorRepository.findById(userId)
                .orElseThrow(
                        () -> new ClientException("student", "student not found id: ".concat(String.valueOf(userId))));
    }
}
