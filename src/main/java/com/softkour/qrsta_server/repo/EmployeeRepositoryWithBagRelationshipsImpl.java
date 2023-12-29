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

import com.softkour.qrsta_server.entity.User;

/**
 * Utility repository to load bag relationships based on
 * https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EmployeeRepositoryWithBagRelationshipsImpl implements EmployeeRepositoryWithBagRelationships {

        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public Optional<User> fetchBagRelationships(Optional<User> employee) {
                return employee.map(this::fetchSessions).map(this::fetchCourses);
        }

        @Override
        public Page<User> fetchBagRelationships(Page<User> employees) {
                return new PageImpl<>(fetchBagRelationships(employees.getContent()), employees.getPageable(),
                                employees.getTotalElements());
        }

        @Override
        public List<User> fetchBagRelationships(List<User> employees) {
                return Optional.of(employees).map(this::fetchSessions).map(this::fetchCourses)
                                .orElse(Collections.emptyList());
        }

        User fetchSessions(User result) {
                return entityManager
                                .createQuery(
                                                "select employee from Employee employee left join fetch employee.sessions where employee.id = :id",
                                                User.class)
                                .setParameter("id", result.getId())
                                .getSingleResult();
        }

        List<User> fetchSessions(List<User> employees) {
                HashMap<Object, Integer> order = new HashMap<>();
                IntStream.range(0, employees.size()).forEach(index -> order.put(employees.get(index).getId(), index));
                List<User> result = entityManager
                                .createQuery(
                                                "select employee from Employee employee left join fetch employee.sessions where employee in :employees",
                                                User.class)
                                .setParameter("employees", employees)
                                .getResultList();
                Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
                return result;
        }

        User fetchCourses(User result) {
                return entityManager
                                .createQuery(
                                                "select employee from Employee employee left join fetch employee.courses where employee.id = :id",
                                                User.class)
                                .setParameter("id", result.getId())
                                .getSingleResult();
        }

        List<User> fetchCourses(List<User> employees) {
                HashMap<Object, Integer> order = new HashMap<>();
                IntStream.range(0, employees.size()).forEach(index -> order.put(employees.get(index).getId(), index));
                List<User> result = entityManager
                                .createQuery(
                                                "select employee from Employee employee left join fetch employee.courses where employee in :employees",
                                                User.class)
                                .setParameter("employees", employees)
                                .getResultList();
                Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
                return result;
        }
}
