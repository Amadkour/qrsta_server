package com.softkour.qrsta_server.payload.request;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.softkour.qrsta_server.entity.course.Course;
import com.softkour.qrsta_server.entity.course.Session;
import com.softkour.qrsta_server.entity.quiz.CourseQuiz;
import com.softkour.qrsta_server.entity.quiz.SessionQuiz;
import com.softkour.qrsta_server.service.SessionService;
import com.softkour.qrsta_server.service.course.CourseService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizCourseSession {
    private Course course;
    private Instant startDate;
    private List<Long> sessionsId;

    public CourseQuiz toCourseQuiz(SessionService sessionService) {
        CourseQuiz covered = new CourseQuiz();
        covered.setStartDate(getStartDate());
        covered.setSessions(sessionService.findAll(sessionsId).stream().map(Session::toSessionQuiz).collect(Collectors.toSet()));
        return covered;
    }

}
