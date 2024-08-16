package com.softkour.qrsta_server.entity.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.softkour.qrsta_server.entity.course.StudentCourse;
import com.softkour.qrsta_server.entity.enumeration.DeviceType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.quiz.StudentQuiz;
import com.softkour.qrsta_server.payload.response.AbstractChild;
import com.softkour.qrsta_server.payload.response.AbstractUser;
import com.softkour.qrsta_server.payload.response.StudntInSession;
import com.softkour.qrsta_server.payload.response.UserLoginResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "qrsta_user")
public class User extends AbstractAuditingEntity {

        @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
        private Student student;

        @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
        private Parent parent;

        @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
        private Teacher teacher;

        @NotNull
        @Column(nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        @Column()
        private UserType type;

        @NotNull
        @Column(nullable = false, unique = true)
        @Size(max = 14, min = 10)
        private String phoneNumber;

        @Column(nullable = true, unique = true)
        @Size(max = 14, min = 9)
        private String nationalId;

        private String otp;
        @NotNull
        private String countryCode;

        @Column()
        private LocalDate dob;

        @Column()
        private String registerMacAddress;
        @Column()
        private String loginMacAddress;

        @Column()
        private String imageUrl;

        @Column()
        private String address;

        @Column(columnDefinition = "boolean default false")
        private boolean isActive;

        @Column(columnDefinition = "boolean default false")
        private boolean isLogged;

        @Column(columnDefinition = "integer default 0")
        private int logoutTimes;
        @Column()
        private String password;
        @Column()
        private String fcmToken;
        @Column()
        private DeviceType deviceType;

        @Column
        private Instant ExpireOTPDateTime;
        @Column
        private Instant ExpirePasswordDate;

        public AbstractUser toAbstractUser() {
                return new AbstractUser(
                                this.getId(), this.getName(), this.getType(), this.getImageUrl(), getPhoneNumber());
        }

        public AbstractChild toAbstractChild() {
                return new AbstractChild(
                                this.getId(), this.getName(), this.getType(), this.getImageUrl(), getPhoneNumber(),
                                getStudent().getCourses().stream().dropWhile(e -> !e.isActive()).count());
        }

        public StudntInSession toStudntInSession(List<Boolean> attendance, boolean isPresent, Long courseId) {
                StudentCourse studentCourse = this.getStudent()
                                .getCourses().stream().filter(e -> Objects.equals(e.getCourse().getId(), courseId)).toList()
                                .get(0);
                Stream<StudentQuiz> studentQuizzes = this.getStudent().getQuizzes().stream()
                                .filter(q -> Objects.equals(q.getCourse().getCourse().getId(), studentCourse.getCourse()
                                        .getId()));
                List<Instant> dInstants = studentCourse.getCourse().getSessions().stream().map(AbstractAuditingEntity::getCreatedDate)
                                .toList();
                int firstIndex = 0;
                for (int i = 0; i < dInstants.size(); i++) {
                        if (dInstants.get(i).isBefore(this.getCreatedDate()))
                                firstIndex = i;
                }
                return new StudntInSession(
                                this.getId(),
                                this.getName(),
                                this.getAddress(),
                                this.getType(),
                                this.getImageUrl(),
                                attendance,
                                isPresent,
                                studentCourse.getAppPaymentLate(),
                                studentCourse.getCoursePaymentLate(),
                                studentCourse.isActive(),
                                studentQuizzes.mapToDouble(StudentQuiz::getGrade).sum(),
                                firstIndex

                );
        }

        public UserLoginResponse toUserLoginResponse() {
                return new UserLoginResponse(
                                getId(),
                                getName(),
                                getType(),
                                getPhoneNumber(),
                                "token",
                                getAddress(),
                                getImageUrl(),
                                (getTeacher() == null) ? null
                                                : (getTeacher().getOrganization() == null) ? null
                                                                : getTeacher().getOrganization().name(),
                                getNationalId(),
                                getCountryCode(),
                                getLoginMacAddress().equalsIgnoreCase(getRegisterMacAddress()),
                                getDob());
        }

        public UserLoginResponse toUpdateResponse() {
                return new UserLoginResponse(
                                getId(),
                                getName(),
                                getType(),
                                getPhoneNumber(),
                                null,
                                getAddress(),
                                getImageUrl(),
                                (getTeacher() == null) ? null : getTeacher().getOrganization().name(),
                                getNationalId(),
                                getCountryCode(),
                                true,
                                getDob());
        }
}