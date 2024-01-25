package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.config.MyUtils;
import com.softkour.qrsta_server.entity.enumeration.UserType;
import com.softkour.qrsta_server.entity.quiz.Quiz;
import com.softkour.qrsta_server.entity.user.User;
import com.softkour.qrsta_server.repo.QuizRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        log.debug("Request to update Quiz : {}", quiz);
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> partialUpdate(Quiz quiz) {
        log.debug("Request to partially update Quiz : {}", quiz);

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
    public List<Quiz> findAll() {
        User u = MyUtils.getCurrentUserSession(authService);
        if (u.getType() == UserType.TEACHER) {
            quizRepository.findAllByCourses_course_teacher_id(u.getId());
        } else {
            quizRepository.findAllByCourses_course_students_student_id(u.getId());

        }
        return quizRepository.findAll();
    }
    public void delete(Long id) {
        log.debug("Request to delete Quiz : {}", id);
        quizRepository.deleteById(id);
    }
}
