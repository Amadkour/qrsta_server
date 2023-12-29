package com.softkour.qrsta_server.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.softkour.qrsta_server.entity.Question;

/**
 * Utility repository to load bag relationships based on
 * https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class QuestionRepositoryWithBagRelationshipsImpl implements QuestionRepositoryWithBagRelationships {

        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public Optional<Question> fetchBagRelationships(Optional<Question> question) {
                return question.map(this::fetchOptions).map(this::fetchQuizzes);
        }

        @Override
        public Page<Question> fetchBagRelationships(Page<Question> questions) {
                return new PageImpl<>(fetchBagRelationships(questions.getContent()), questions.getPageable(),
                                questions.getTotalElements());
        }

        @Override
        public List<Question> fetchBagRelationships(List<Question> questions) {
                return Optional.of(questions).map(this::fetchOptions).map(this::fetchQuizzes)
                                .orElse(Collections.emptyList());
        }

        Question fetchOptions(Question result) {
                return entityManager
                                .createQuery(
                                                "select question from Question question left join fetch question.options where question.id = :id",
                                                Question.class)
                                .setParameter("id", result.getId())
                                .getSingleResult();
        }

        List<Question> fetchOptions(List<Question> questions) {
                HashMap<Object, Integer> order = new HashMap<>();
                IntStream.range(0, questions.size()).forEach(index -> order.put(questions.get(index).getId(), index));
                List<Question> result = entityManager
                                .createQuery(
                                                "select question from Question question left join fetch question.options where question in :questions",
                                                Question.class)
                                .setParameter("questions", questions)
                                .getResultList();
                Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
                return result;
        }

        Question fetchQuizzes(Question result) {
                return entityManager
                                .createQuery(
                                                "select question from Question question left join fetch question.quizzes where question.id = :id",
                                                Question.class)
                                .setParameter("id", result.getId())
                                .getSingleResult();
        }

        List<Question> fetchQuizzes(List<Question> questions) {
                HashMap<Object, Integer> order = new HashMap<>();
                IntStream.range(0, questions.size()).forEach(index -> order.put(questions.get(index).getId(), index));
                List<Question> result = entityManager
                                .createQuery(
                                                "select question from Question question left join fetch question.quizzes where question in :questions",
                                                Question.class)
                                .setParameter("questions", questions)
                                .getResultList();
                Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
                return result;
        }
}
