package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.quiz.Option;
import com.softkour.qrsta_server.repo.OptionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionService {

    private final Logger log = LoggerFactory.getLogger(OptionService.class);

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Option save(Option option) {
        log.debug("Request to save Option : {}", option);
        return optionRepository.save(option);
    }

    public Option update(Option option) {
        log.debug("Request to update Option : {}", option);
        return optionRepository.save(option);
    }

    public Optional<Option> partialUpdate(Option option) {
        log.debug("Request to partially update Option : {}", option);

        return optionRepository
                .findById(option.getId())
                .map(existingOption -> {
                    if (option.getId() != null) {
                        existingOption.setId(option.getId());
                    }
                    if (option.getTitle() != null) {
                        existingOption.setTitle(option.getTitle());
                    }
                    if (option.getIsCorrectAnswer() != null) {
                        existingOption.setIsCorrectAnswer(option.getIsCorrectAnswer());
                    }

                    return existingOption;
                })
                .map(optionRepository::save);
    }

    @Transactional(readOnly = true)
    public List<Option> findAll() {
        log.debug("Request to get all Options");
        return optionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Option> findOne(Long id) {
        log.debug("Request to get Option : {}", id);
        return optionRepository.findById(id);
    }


    public void delete(Long id) {
        log.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }
}
