package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.Option;
import com.softkour.qrsta_server.repo.OptionRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softkour.qrsta_server.domain.Option}.
 */
@Service
@Transactional
public class OptionService {

    private final Logger log = LoggerFactory.getLogger(OptionService.class);

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * Save a option.
     *
     * @param option the entity to save.
     * @return the persisted entity.
     */
    public Option save(Option option) {
        log.debug("Request to save Option : {}", option);
        return optionRepository.save(option);
    }

    /**
     * Update a option.
     *
     * @param option the entity to save.
     * @return the persisted entity.
     */
    public Option update(Option option) {
        log.debug("Request to update Option : {}", option);
        return optionRepository.save(option);
    }

    /**
     * Partially update a option.
     *
     * @param option the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Option> partialUpdate(Option option) {
        log.debug("Request to partially update Option : {}", option);

        return optionRepository
                .findById(option.getId())
                .map(existingOption -> {
                    if (option.getOptionId() != null) {
                        existingOption.setOptionId(option.getOptionId());
                    }
                    if (option.getName() != null) {
                        existingOption.setName(option.getName());
                    }
                    if (option.getIsCorrectAnswer() != null) {
                        existingOption.setIsCorrectAnswer(option.getIsCorrectAnswer());
                    }

                    return existingOption;
                })
                .map(optionRepository::save);
    }

    /**
     * Get all the options.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Option> findAll() {
        log.debug("Request to get all Options");
        return optionRepository.findAll();
    }

    /**
     * Get one option by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Option> findOne(Long id) {
        log.debug("Request to get Option : {}", id);
        return optionRepository.findById(id);
    }

    /**
     * Delete the option by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }
}
