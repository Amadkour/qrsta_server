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
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

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
        if (authorRepository.existsByPhoneNumberAndIsActive(request.getPhone(), false)) {
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
            if (!authorRepository.existsByPhoneNumberAndIsActive(request.getPhone(), true)) {
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
            if (authorRepository.existsByPhoneNumberAndIsActive(request.getPhone(), true)) {
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
            Student s = user.getStudent();
            s.setNeedToReplace(false);
            user.setStudent(s);
            authorRepository.save(user);
        }
        return "cancle request successfully";

    }

    public User getUserByPhoneNumber(String phoneNumber) {
        return authorRepository.findUserByPhoneNumber(phoneNumber);
    }

    public List<User> getNeedToReplaceUsers(Long teacherId) {
        return authorRepository.findAllUserByStudent_needToReplaceAndCourses_course_teacher_id(false, teacherId);
    }

    public User save(User user) {
        return authorRepository.save(user);
    }

    public List<User> getChildren(AuthService authService) {
        return authorRepository.findAllChildrenByStudent_parent_id(
                MyUtils.getCurrentUserSession(authService).getId());
    }

    public User getParent(AuthService authService) {
        User u = MyUtils.getCurrentUserSession(authService);
        if (u.getStudent() == null) {
            throw new ClientException("parent", "this instance not a student");
        }
        return u.getStudent().getParent();
    }

    public User deleteChild(Long childId) {
        User u = authorRepository.findById(childId).orElseThrow(() -> new ClientException("user", "parent not found"));
        u.setParent(null);
        return authorRepository.save(u);
    }

    public User addChild(AuthService authService, String childId) {
        User child = authorRepository.findUserByPhoneNumber(childId);
        if (child.getStudent() == null)
            throw new ClientException("parent", "this instance not a student");
        Student s = child.getStudent();
        s.setParent(MyUtils.getCurrentUserSession(authService));
        child.setStudent(s);
        return authorRepository.save(child);
    }

    public User verifyParentUser(String otp, String parentPhone, User currentUser) {
        User parentUser = getUserByPhoneNumber(parentPhone);
        if (parentUser.getOtp().equalsIgnoreCase(otp) && !parentUser.getExpireOTPDateTime().isBefore(Instant.now())) {
            parentUser.setActive(true);
            parentUser = save(parentUser);
            if (currentUser.getStudent() == null)
                throw new ClientException("parent", "this instance not a student");
            Student s = currentUser.getStudent();
            s.setParent(parentUser);
            currentUser.setStudent(s);
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

    public double getUserScore(long userId, Long courseId) {
        if (authorRepository.findById(userId).get().getQuizzes().isEmpty())
            return 0.0;
        else {
            return authorRepository.getScoreByIdAndQuizzes_quiz_session_course_id(userId, courseId).getQuizzes()
                    .stream()
                    .mapToDouble(e -> e.getGrade()).sum();
        }

    }

    public int getUserLatePayment(long userId, Long courseId) {
        return authorRepository.getScoreByIdAndCourses_course_id(courseId, courseId).getCourses().iterator().next()
                .getLate();
    }

    public int getMissedParents() {
        return authorRepository.getStudentByStudent_parent_password(null).size();

    }

    public int getAllStudent(Long teacherId) {
        return authorRepository.getStudentByCourses_course_teacher_id(teacherId).size();

    }

    public boolean sendMessageToPhone(String phone, String message) {

        try {
            Twilio.init("ACbe5e5fed0c2829d18a709fd66de88ae9", "ffdfada27c184f64898ac59dfd1236af");

            PhoneNumber to = new PhoneNumber(phone);
            PhoneNumber from = new PhoneNumber("QRSTA");
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            return true;

        } catch (Exception e) {
            log.warn(e.toString());
            return false;
        }
    }
    // public int getMissedStudentsPayment(Long teacherId) {
    // return
    // authorRepository.getStudentByCourses_course_teacher_idAndCourses_lateGreterThan(teacherId,
    // 1).size();
    //
    // }

    public List<Boolean> getUserAttendance(long userId, Long courseId) {
        return authorRepository.getScoreByIdAndCourses_course_id(courseId, courseId).getCourses().iterator().next()
                .getCourse().getSessions().stream()
                .map(s -> s.getStudents().stream()
                        .anyMatch(b -> b.getId() == userId))
                .toList();
    }
}
