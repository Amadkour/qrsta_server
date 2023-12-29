package com.softkour.qrsta_server.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.softkour.qrsta_server.entity.Quiz;

/**
 * Utility repository to load bag relationships based on
 * https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class QuizRepositoryWithBagRelationshipsImpl implements QuizRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Quiz> fetchBagRelationships(Optional<Quiz> quiz) {
        return quiz.map(this::fetchSessions);
    }

    @Override
    public Page<Quiz> fetchBagRelationships(Page<Quiz> quizzes) {
        return new PageImpl<>(fetchBagRelationships(quizzes.getContent()), quizzes.getPageable(),
                quizzes.getTotalElements());
    }

    @Override
    public List<Quiz> fetchBagRelationships(List<Quiz> quizzes) {
        return Optional.of(quizzes).map(this::fetchSessions).orElse(Collections.emptyList());
    }

    Quiz fetchSessions(Quiz result) {
        return entityManager
                .createQuery("select quiz from Quiz quiz left join fetch quiz.sessions where quiz.id = :id", Quiz.class)
                .setParameter("id", result.getId())
                .getSingleResult();
    }

    List<Quiz> fetchSessions(List<Quiz> quizzes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, quizzes.size()).forEach(index -> order.put(quizzes.get(index).getId(), index));
        List<Quiz> result = entityManager
                .createQuery("select quiz from Quiz quiz left join fetch quiz.sessions where quiz in :quizzes",
                        Quiz.class)
                .setParameter("quizzes", quizzes)
                .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
