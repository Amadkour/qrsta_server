package com.softkour.qrsta_server.payload.request;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import com.softkour.qrsta_server.entity.quiz.CourseQuiz;

import com.softkour.qrsta_server.service.SessionService;
import com.softkour.qrsta_server.service.course.CourseService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizCourseSession {
    private Long courseId;
    private Instant startDate;
    private List<Long> sessionsId;

    public CourseQuiz toCourseQuiz(SessionService sessionService,CourseService courseService) {
        CourseQuiz covered = new CourseQuiz();
        covered.setStartDate(getStartDate());
        covered.setCourse(courseService.findOne(getCourseId()));
        covered.setSessions(new HashSet<>(sessionService.findAll(sessionsId)));
        return covered;
    }
}
