package com.softkour.qrsta_server.entity.course;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.quiz.CourseQuiz;
import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.entity.quiz.StudentQuiz;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.Student;
import com.softkour.qrsta_server.payload.response.SessionDateAndStudentGrade;
import com.softkour.qrsta_server.payload.response.SessionDetailsStudent;
import com.softkour.qrsta_server.payload.response.SessionDetailsWithoutStudents;
import com.softkour.qrsta_server.payload.response.SessionNameAndId;
import com.softkour.qrsta_server.payload.response.SessionObjectResponse;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session extends AbstractAuditingEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user__session", joinColumns = @JoinColumn(name = "session_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    @JsonIgnoreProperties(value = {"sessions", "courses", "offers", "needToReplace"}, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "sessions", cascade = CascadeType.PERSIST)
    private Set<CourseQuiz> courseQuizzes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    @JsonIgnoreProperties(value = {"session"}, allowSetters = true)
    private Set<SessionObject> objects = new HashSet<>();

    @Column()
    private Instant startDate;

    @Column(columnDefinition = "boolean default false")
    private boolean finish;

    @Column()
    private String label;
    @Column()
    private Instant endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"sessions", "schedules"}, allowSetters = true)
    private Course course;

    public void setStudents(Set<Student> students) {
        if (students != null) {
            students.forEach(i -> i.removeSession(this));
            students.forEach(i -> i.addSession(this));
        }
        this.students = students;
    }

    public Session addStudent(Student student) {
        students.add(student);
        return this;
    }

    public void removeStudent(Student employee) {
        students.remove(employee);
        employee.getSessions().remove(this);
    }

    public SessionDateAndStudentGrade toQuizSession() {

        Instant now = Instant.now();
        return new SessionDateAndStudentGrade(
                null,
                null,
                getId(),
                getLabel(),
                null,
                null,
                null,
                null,
                null);
    }

    public SessionDateAndStudentGrade toSessionDateAndStudentGrade(Long studentId) {
        double grade = 0;
        int totalGrade = 0;
        System.out.println("==================" + getCourseQuizzes().size() + "================");
//        for (CourseQuiz sessionQuiz : getCourseQuizzes()) {
//            System.out.println(sessionQuiz.getSessions().size()+"wwwwwwwwwwwwwww");
//            System.out.println(sessionQuiz.getQuiz().getId());
//            if (!sessionQuiz.getStudents().isEmpty() && !sessionQuiz.getQuiz().getQuestions().isEmpty()) {
//                grade += sessionQuiz.getStudents().stream().mapToDouble(StudentQuiz::getGrade).reduce(0, Double::sum);
//                totalGrade += sessionQuiz.getQuiz().getQuestions().stream().map(Question::getGrade).reduce(0, Integer::sum);
//            }
//        }
        try {
            CourseQuiz sessionQuiz = getCourseQuizzes().iterator().next();
            if (!sessionQuiz.getStudents().isEmpty() && !sessionQuiz.getQuiz().getQuestions().isEmpty()) {
                grade += sessionQuiz.getStudents().stream().mapToDouble(StudentQuiz::getGrade).reduce(0, Double::sum);
                totalGrade += sessionQuiz.getQuiz().getQuestions().stream().map(Question::getGrade).reduce(0, Integer::sum);

            }
        } catch (Exception ignored) {

        }
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
                grade / totalGrade,
                now.isAfter(getEndDate()) || isFinish(),
                getStudents().stream().anyMatch(e -> Objects.equals(e.getUser().getId(), studentId)));
    }

    public SessionDateAndStudentGrade toSessionDateAndStudentGradeWithAttendance(Boolean attendance) {
        double grade = 0;
        int totalGrade = 0;
        for (CourseQuiz sessionQuiz : getCourseQuizzes()) {
            grade = sessionQuiz.getStudents().stream().mapToDouble(StudentQuiz::getGrade).reduce(0, Double::sum);
            totalGrade = sessionQuiz.getQuiz().getQuestions().stream().map(Question::getGrade).reduce(0, Integer::sum);
        }
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
                grade / totalGrade,
                now.isAfter(getEndDate()) || isFinish(),

                attendance != null && attendance);
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
                        .map((e) -> e.getStudent().getUser().toStudntInSession(
                                /// attendance
                                sessions.stream()
                                        .map(s -> s.getStudents().stream()
                                                .anyMatch(b -> Objects.equals(b.getId(), e
                                                        .getStudent()
                                                        .getId())))
                                        .toList(),
                                /// isPresent in this Session?
                                getStudents().stream()
                                        .anyMatch(m -> Objects.equals(m.getId(), e
                                                .getStudent().getId())),
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
