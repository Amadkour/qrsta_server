package com.softkour.qrsta_server.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.quiz.Question;
import com.softkour.qrsta_server.entity.quiz.Quiz;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.exception.ClientException;
import com.softkour.qrsta_server.repo.QuizRepository;

@Service
@Transactional
public class QuizService {

    @Autowired
    AuthService authService;

    private final Logger log = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz save(Quiz quiz) {
        log.debug("Request to save Quiz : {}", quiz);
        return quizRepository.save(quiz);
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
                    if (quiz.getStartDate() != null) {
                        existingQuiz.setStartDate(quiz.getStartDate());
                    }
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
            quizRepository.findAllByCourses_course_teacher_id(u.getId());
        } else {
            quizRepository.findAllByCourses_course_students_student_id(u.getId());

        }
        return quizRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(() -> new ClientException("quiz", "not found"));
    }

    @Transactional(readOnly = true)
    public String correct(List<List<String>> answers, Long quizId) {
        Quiz q = quizRepository.findById(quizId).orElseThrow(() -> new ClientException("quiz", "not found"));
        List<Question> questions = q.getQuestions().stream().toList();
        int totalPoints = questions.stream().mapToInt(e -> e.getGrade()).sum();
        int points = 0;
        log.warn("========================answers");
        log.warn(String.valueOf(questions.size()));
        log.warn(String.valueOf(answers.size()));
        log.warn("========================answers");

        for (int i = 0; i < questions.size(); i++) {
            List<String> correctAnswer = questions.get(i).getOptions().stream().takeWhile(e -> e.getIsCorrectAnswer())
                    .map(e -> e.getTitle()).toList();

            if (answers.get(i).stream().allMatch(e -> correctAnswer.contains(e))
                    && answers.get(i).size() == correctAnswer.size()) {
                points += questions.get(i).getGrade();
            }
        }

        return String.valueOf(points) + '/' + String.valueOf(totalPoints);

    }

    public void delete(Long id) {
        quizRepository.deleteById(id);
    }
}
