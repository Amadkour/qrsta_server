package com.softkour.qrsta_server.service;

import java.util.*;

import com.softkour.qrsta_server.entity.quiz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.course.StudentCourse;
import com.softkour.qrsta_server.entity.enumeration.NotificationType;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.public_entity.StudentSchedule;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.QuizRepository;
import com.softkour.qrsta_server.repo.StudentScheduleRepo;
import com.softkour.qrsta_server.service.public_service.NotificationService;

@Service
@Transactional
public class QuizService {

    @Autowired
    AuthService authService;
    @Autowired
    StudentScheduleRepo scheduleRepo;
    @Autowired
    NotificationService notificationService;

    private final Logger log = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }
@Transactional
    public Quiz save(Quiz quiz) {
        quiz = quizRepository.save(quiz);
        Set<User> users = new HashSet<>();
        for (CourseQuiz c : quiz.getCourses()) {
            for (StudentCourse u : c.getSessions().iterator().next().getSession().getCourse().getStudents()) {
                users.add(u.getStudent().getUser());
            }
        }
        notificationService.addNotification(
                NotificationType.QUIZ, "there are a new exam successfully and will start in " + quiz.getCourses().stream().map(e -> e.getSessions().iterator().next().getSession().getCourse().getName()),
                quiz.getId(),
                users);

        return quiz;
    }

    public Quiz update(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> partialUpdate(Quiz quiz) {

        return quizRepository
                .findById(quiz.getId())
                .map(existingQuiz -> {
                    if (quiz.getId() != null) {
                        existingQuiz.setId(quiz.getId());
                    }
                    if (quiz.getCreatedDate() != null) {
                        existingQuiz.setCreatedDate(quiz.getCreatedDate());
                    }
                    // if (quiz.getStartDate() != null) {
                    // existingQuiz.setStartDate(quiz.getStartDate());
                    // }
                    if (quiz.getQuestionsPerStudent() != null) {
                        existingQuiz.setQuestionsPerStudent(quiz.getQuestionsPerStudent());
                    }
                    if (quiz.getType() != null) {
                        existingQuiz.setType(quiz.getType());
                    }

                    return existingQuiz;
                })
                .map(quizRepository::save);
    }

    /**
     * Get all the quizzes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Quiz> findAll(String phone) {
        User u;
        if (phone != null) {
            u = authService.getUserByPhoneNumber(phone);
        } else {
            u = MyUtils.getCurrentUserSession(authService);
        }
        if (u.getType() == UserType.TEACHER) {
            quizRepository.findAllByCourses_sessions_session_course_teacher_id(u.getId());
        } else {
            quizRepository.findAllByCourses_sessions_session_course_students_student_id(u.getId());

        }
        return quizRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(() -> new ClientException("quiz", "not found"));
    }

    public String correct(List<List<String>> answers, Long quizId) {
        Quiz q = quizRepository.findById(quizId).orElseThrow(() -> new ClientException("quiz", "not found"));
        List<Question> questions = q.getQuestions().stream().toList();
        List<Question> wrongQuestions = new ArrayList<>();
        User u = MyUtils.getCurrentUserSession(authService);
        int totalPoints = questions.stream().mapToInt(Question::getGrade).sum();
        int points = 0;
        for (int i = 0; i < questions.size(); i++) {
            List<String> correctAnswer = questions.get(i).getOptions().stream().takeWhile(Option::getIsCorrectAnswer)
                    .map(Option::getTitle).toList();
            System.out.println(answers.get(i).size() == correctAnswer.size());
            if (answers.get(i).stream().allMatch(e -> correctAnswer.contains(e.replace("[", "").replace("]", "")))
                    && answers.get(i).size() == correctAnswer.size()) {
                points += questions.get(i).getGrade();
            } else {
                wrongQuestions.add(questions.get(i));
            }
        }
        ////
        /// add it in student schedual
        log.warn("total score is:" + (points / totalPoints));
        log.warn("total of wrong answers:" + wrongQuestions.size());
        if (((double) points / totalPoints) < 0.5) {
            List<CourseQuiz> courses = q.getCourses().stream().toList();
            // for (int i = 0; i < q.getCoveredSessions().size(); i++) {
            CourseQuiz c = courses.stream()
                    .takeWhile(e -> e.getSessions().iterator().next().getSession().getCourse().getStudents().stream().anyMatch(s -> Objects.equals(s.getId(), u.getId())))
                    .findFirst().orElseThrow(() -> new ClientException("course", "user unjoint"));
            List<SessionQuiz> sessions = c.getSessions().stream().toList();
            for (SessionQuiz session : sessions) {
                log.warn("add to students" + u.getPhoneNumber());

                StudentSchedule item = new StudentSchedule();
                item.setDone(false);
                item.setRead(false);
                item.setCourse(c.getSessions().iterator().next().getSession().getCourse());
                item.setSession(session.getSession());
                item.setUser(u);
                item.setQuestion(wrongQuestions.get((new Random()).nextInt(wrongQuestions.size())));
                scheduleRepo.save(item);
            }
        }
        // }
        return String.valueOf(points) + '/' + String.valueOf(totalPoints);

    }

    public void delete(Long id) {
        quizRepository.deleteById(id);
    }
}
