package com.softkour.qrsta_server.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

import com.softkour.qrsta_server.entity.Question;

public interface QuestionRepositoryWithBagRelationships {
    Optional<Question> fetchBagRelationships(Optional<Question> question);

    List<Question> fetchBagRelationships(List<Question> questions);

    Page<Question> fetchBagRelationships(Page<Question> questions);
}
