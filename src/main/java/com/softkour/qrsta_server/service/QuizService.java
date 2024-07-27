package com.softkour.qrsta_server.service;

import java.util.*;

import com.softkour.qrsta_server.entity.enumeration.EssayCorrectionType;
import com.softkour.qrsta_server.entity.enumeration.QuestionType;
import com.softkour.qrsta_server.entity.quiz.*;
import com.softkour.qrsta_server.payload.request.QuizCorrectionRequest;
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

    public String correct(QuizCorrectionRequest quizCorrectionRequest) {
        Quiz q = quizRepository.findById(quizCorrectionRequest.getQuizId()).orElseThrow(() -> new ClientException("quiz", "not found"));
        List<Question> questions = q.getQuestions().stream().takeWhile(e -> quizCorrectionRequest.getQuestionsId().contains(e.getId())).toList();
        //=================================
        List<Question> wrongQuestions = new ArrayList<>();
        User u = MyUtils.getCurrentUserSession(authService);
        int totalPoints = questions.stream().mapToInt(Question::getGrade).sum();
        int points = 0;
        for (int i = 0; i < questions.size(); i++) {
            List<String> correctAnswer = questions.get(i).getOptions().stream().takeWhile(Option::getIsCorrectAnswer).map(Option::getTitle).toList();
            if (questions.get(i).getType() == QuestionType.TRUEFALSE) {
                if (!quizCorrectionRequest.getAnswers().get(i).isEmpty() && Objects.equals(correctAnswer.get(0), quizCorrectionRequest.getAnswers().get(i).get(0))) {
                    points += questions.get(i).getGrade();
                } else {
                    wrongQuestions.add(questions.get(i));

                }
            } else if (questions.get(i).getType() == QuestionType.MCQ) {

                if (!quizCorrectionRequest.getAnswers().get(i).isEmpty() &&
                        new HashSet<>(correctAnswer).containsAll(quizCorrectionRequest.getAnswers().get(i)) &&
                        quizCorrectionRequest.getAnswers().get(i).size() == correctAnswer.size()) {
                    points += questions.get(i).getGrade();
                } else {
                    wrongQuestions.add(questions.get(i));

                }
            }
            else if (questions.get(i).getType() == QuestionType.SPACE) {
               if(new HashSet<>(quizCorrectionRequest.getAnswers().get(i)).containsAll(correctAnswer)) {
                    points += questions.get(i).getGrade();
                } else {
                    wrongQuestions.add(questions.get(i));
                }
            }

            else {
                EssayCorrectionType correctionType = questions.get(i).getCorrectionType();
                if (correctionType == EssayCorrectionType.MATCHINGAI && Objects.equals(correctAnswer.get(0), quizCorrectionRequest.getAnswers().get(i).get(0))) {
                        points += questions.get(i).getGrade();
                } else if (correctionType == EssayCorrectionType.KEYWORDS && new HashSet<>(quizCorrectionRequest.getAnswers().get(i)).containsAll(correctAnswer)) {
                    points += questions.get(i).getGrade();
                } else {
                    wrongQuestions.add(questions.get(i));
                }
            }
        }

        /// add it in student schedule
        System.out.println("total of wrong answers:" + wrongQuestions.size());
        if (((double) points / totalPoints) < 0.5) {
            List<CourseQuiz> courses = q.getCourses().stream().toList();
            // for (int i = 0; i < q.getCoveredSessions().size(); i++) {
            CourseQuiz c = courses.stream()
                    .takeWhile(e -> e.getSessions().iterator().next().getSession().getCourse().getStudents().stream().anyMatch(s -> Objects.equals(s.getId(), u.getId())))
                    .findFirst().orElseThrow(() -> new ClientException("course", "user unjoint"));
            List<SessionQuiz> sessions = c.getSessions().stream().toList();
            for (SessionQuiz session : sessions) {
                System.out.println("add to students" + u.getPhoneNumber());

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
        return String.valueOf(points) + '/' + totalPoints;

    }

    public void delete(Long id) {
        quizRepository.deleteById(id);
    }
}
