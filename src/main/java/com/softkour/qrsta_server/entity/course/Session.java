package com.softkour.qrsta_server.entity.course;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.quiz.SessionQuiz;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.response.SessionDateAndStudentGrade;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;
import com.softkour.qrsta_server.payload.response.SessionDetailsWithoutStudents;
import com.softkour.qrsta_server.payload.response.SessionNameAndId;
import com.softkour.qrsta_server.payload.response.SessionObjectResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Slf4j
public class Session extends AbstractAuditingEntity {

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user__session", joinColumns = @JoinColumn(name = "session_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
        @JsonIgnoreProperties(value = { "sessions", "courses" }, allowSetters = true)
        private Set<User> students = new HashSet<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
        @JsonIgnoreProperties(value = { "sessions", "quizzes" }, allowSetters = true)
        private Set<SessionQuiz> quizzes = new HashSet<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
        @JsonIgnoreProperties(value = { "session" }, allowSetters = true)
        private Set<SessionObject> objects = new HashSet<>();

        @Column()
        private Instant startDate;

        @Column()
        private String label;
        @Column()
        private Instant endDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JsonIgnoreProperties(value = { "sessions", "schedules" }, allowSetters = true)
        private Course course;

        public void setStudents(Set<User> students) {
                if (students != null) {
                        students.forEach(i -> i.removeSession(this));
                        students.forEach(i -> i.addSession(this));
                }
                this.students = students;
        }

        public Session addStudent(User student) {
                students.add(student);
                return this;
        }

        public Session removeStudent(User employee) {
                students.remove(employee);
                employee.getSessions().remove(this);
                return this;
        }

        public SessionDateAndStudentGrade toSessionDateAndStudentGrade(Long studentId) {
                double grade = getQuizzes().stream().reduce((first, second) -> second)
                                .orElse(new SessionQuiz(null)).getStudents()
                                .stream().flatMapToDouble(s -> DoubleStream.of(s.getGrade())).average().orElse(0);
                Instant now = Instant.now();
                return new SessionDateAndStudentGrade(
                                TimeUnit.MINUTES.convert(getStartDate().toEpochMilli() - now.toEpochMilli(),
                                                TimeUnit.MILLISECONDS),
                                TimeUnit.MINUTES.convert(getEndDate().toEpochMilli() - getStartDate().toEpochMilli(),
                                                TimeUnit.MILLISECONDS),
                                getId(),
                                getLabel(),
                                getStudents().size(),
                                getCourse().getStudents().size(),
                                grade,
                                now.isAfter(getEndDate()),
                                getStudents().stream().anyMatch(e -> e.getId() == studentId));
        }

        public SessionDateAndStudentGrade toSessionDateAndStudentGradeWithAttendance(Boolean attendance) {
                double grade = getQuizzes().stream().reduce((first, second) -> second)
                                .orElse(new SessionQuiz(null)).getStudents()
                                .stream().flatMapToDouble(s -> DoubleStream.of(s.getGrade())).average().orElse(0);
                Instant now = Instant.now();
                return new SessionDateAndStudentGrade(
                                TimeUnit.MINUTES.convert(getStartDate().toEpochMilli() - now.toEpochMilli(),
                                                TimeUnit.MILLISECONDS),
                                TimeUnit.MINUTES.convert(getEndDate().toEpochMilli() - getStartDate().toEpochMilli(),
                                                TimeUnit.MILLISECONDS),
                                getId(),
                                getLabel(),
                                getStudents().size(),
                                getCourse().getStudents().size(),
                                grade,
                                now.isAfter(getEndDate()),

                                attendance == null ? false : attendance);
        }

        public SessionNameAndId toSessionNameAndId() {
                return new SessionNameAndId(getId(), getLabel());
        }

        public SessionDetailsStudent toSessionDetailsStudent() {

                Set<Session> sessions = getCourse().getSessions();
                // getStudents().stream().anyMatch(m -> m.getId() ==
                // e.getStudent().getId())
                return new SessionDetailsStudent(
                                getCourse().getStudents().stream()
                                                .map((e) -> e.getStudent().toStudntInSession(
                                                                /// attendance
                                                                sessions.stream()
                                                                                .map(s -> s.getStudents().stream()
                                                                                                .anyMatch(b -> b.getId() == e
                                                                                                                .getStudent()
                                                                                                                .getId()))
                                                                                .toList(),
                                                                /// isPresent in this Session?
                                                                getStudents().stream()
                                                                                .anyMatch(m -> m.getId() == e
                                                                                                .getStudent().getId()),
                                                                /// course
                                                                getCourse().getId()))
                                                .toList());

        }

        public SessionDetailsWithoutStudents toSessionDetailsWithoutStudents() {

                return new SessionDetailsWithoutStudents(
                                getObjects().stream()
                                                .map((e) -> new SessionObjectResponse(e.getTitle(),
                                                                e.getSubItems().stream()
                                                                                .map((s) -> new SessionObjectResponse(
                                                                                                s.getTitle(),
                                                                                                null, s.getType(),
                                                                                                s.getCreatedDate(),
                                                                                                s.getId()))
                                                                                .toList(),
                                                                e.getType(), e.getCreatedDate(), e.getId()))
                                                .toList());
        }
}
