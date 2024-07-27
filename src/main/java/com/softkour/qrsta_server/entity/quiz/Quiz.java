package com.softkour.qrsta_server.entity.quiz;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softkour.qrsta_server.entity.enumeration.QuizType;
import com.softkour.qrsta_server.entity.user.AbstractAuditingEntity;
import com.softkour.qrsta_server.payload.request.OptionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuestionCreationRequest;
import com.softkour.qrsta_server.payload.request.QuizCourseSession;
import com.softkour.qrsta_server.payload.request.QuizCreationRequest;
import com.softkour.qrsta_server.payload.response.QuizResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.IntStream;

@Entity
@Setter
@Getter
public class Quiz extends AbstractAuditingEntity {


    @Column()
    private String questionsPerStudent;

    @Column()
    private String code;

    @Column()
    private String timePerMinutes;

    @Enumerated(EnumType.STRING)
    @Column()
    private QuizType type;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"sessions"}, allowSetters = true)
    private Set<CourseQuiz> courses = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "quizzes", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"options", "quizzes"}, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.removeQuiz(this));
        }
        if (questions != null) {
            questions.forEach(i -> i.addQuiz(this));
        }
        this.questions = questions;
    }


    public QuizResponse toQuizModel() {
        QuizResponse quizResponse = new QuizResponse();
        if (getType() == QuizType.ONLINE) {
            quizResponse.setStartDate(getCourses().iterator().next().getStartDate());
        } else {
            if (getType() == QuizType.BEGIN) {
                quizResponse.setStartDate(
                        getCourses().iterator().next().getSessions().stream()
                                .toList().get(0)
                                .getSession().getStartDate());
            } else if (getType() == QuizType.END) {
                quizResponse.setStartDate(
                        getCourses().iterator().next().getSessions().stream()
                                .toList().get(0)
                                .getSession().getEndDate().minusSeconds(Integer.parseInt(getTimePerMinutes()) * 60L));
            }
        }
        quizResponse.setCourses(getCourses().stream().map(e->e.getSessions().iterator().next().getSession().getCourse().getName()).toList());
        quizResponse.setPoints(getQuestions().stream().mapToInt(Question::getGrade).sum());
        quizResponse.setId(getId());
        quizResponse.setQuestionCount(getQuestionsPerStudent());
        quizResponse.setStudentCount(
                getCourses().stream().mapToInt(e -> e.getSessions().iterator().next().getSession().getCourse().getStudents().size()).sum());
        quizResponse.setType(getType());
        quizResponse.setTimePerMinutes(getTimePerMinutes());
        quizResponse.setCode(getCode());

        return quizResponse;

    }

    public List<QuestionCreationRequest> toStudentQuiz() {
        Random rand = new Random();
        List<QuestionCreationRequest> questions=new ArrayList<>();
        List<Question> origin=getQuestions().stream().toList();
        System.out.println(origin.size());
        for(int i=0;i<Integer.parseInt(getQuestionsPerStudent());i++){
            Question q =origin.get(rand.nextInt(origin.size()));
            QuestionCreationRequest selectedQuestion= new QuestionCreationRequest();
            selectedQuestion.setId(q.getId());
            selectedQuestion.setTitle(q.getTitle());
            selectedQuestion.setGrade(q.getGrade());
            selectedQuestion.setOptions(     q.getOptions().stream()
                    .map(o -> new OptionCreationRequest(o.getTitle(), false)).toList());
            selectedQuestion.setCoveredSessions(q.getCoveredSessions().stream().map(CourseQuiz::toQuizCourseSession).toList());
            selectedQuestion.setQuestionType(q.getType());
            selectedQuestion.setCorrectionType(q.getCorrectionType());
            questions.add(selectedQuestion);
        }
        return questions;

    }

    public QuizCreationRequest toTeacherQuiz() {
        QuizCreationRequest quiz = new QuizCreationRequest();
        quiz.setCourses(getCourses().stream().map(e -> new QuizCourseSession(
                        e.getSessions().iterator().next().getSession().getCourse().getId(),
                        e.getStartDate(),
                        e.getSessions().stream().map(s -> s.getSession().getId()).toList()))
                .collect(Collectors.toSet()));
        quiz.setQuestions(
                getQuestions().stream().map(Question::toTeacher)
                        .collect(Collectors.toSet()));
        return quiz;
    }
}
