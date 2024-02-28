package com.softkour.qrsta_server.entity.public_entity;

import java.time.Instant;
import java.util.stream.Collectors;

import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.payload.request.OptionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuestionCreationRequest;
import com.softkour.qrsta_server.payload.response.StudentSchedualResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StudentSchedule extends AbstractAuditingEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Session session;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Course course;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Question question;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
    @Column(name = "due_date", updatable = false)
    private Instant due_date = Instant.now().plusSeconds(60 * 60 * 24 * 7);
    @Column(columnDefinition = "boolean default false")
    private boolean read;
    @Column
    private int weight;
    @Column(columnDefinition = "boolean default false")
    private boolean done;
    @Column(columnDefinition = "boolean default true")
    private boolean active;

    public StudentSchedualResponse toStudentSchedualResponse() {

        StudentSchedualResponse response = new StudentSchedualResponse(
                getId(),
                getDue_date(),
                getWeight(),
                getSession().toSessionDateAndStudentGrade(user.getId()),
                getCourse().getName(),
                isRead(),
                isDone(),
                getCreatedDate(),
                                        null);
                                if (getQuestion() != null) {
                                    response.setQuestion(new QuestionCreationRequest(getQuestion().getId(),
                                            getQuestion().getTitle(), getQuestion().getGrade(),
                                            getQuestion().getOptions().stream()
                                                    .map(o -> new OptionCreationRequest(o.getTitle(),
                                                            o.getIsCorrectAnswer()))
                                                    .collect(Collectors.toSet())));
                                }
                                return response;
    }

}
