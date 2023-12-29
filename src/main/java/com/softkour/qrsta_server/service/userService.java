package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.User;
import java.util.Optional;

import com.softkour.qrsta_server.repo.UserRepository;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class userService {
    private final Logger log = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    UserRepository userRepository;

    /**
     * Save a user.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Update a user.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    public User update(User user) {
        log.debug("Request to update user : {}", user);
        return userRepository.save(user);
    }

    /**
     * Partially update a user.
     *
     * @param user the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<User> partialUpdate(User user) {
        log.debug("Request to partially update user : {}", user);

        return userRepository
                .findById(user.getId())
                .map(existinguser -> {
                    if (user.getId() != null) {
                        existinguser.setId(user.getId());
                    }
                    if (user.getName() != null) {
                        existinguser.setName(user.getName());
                    }
                    if (user.getType() != null) {
                        existinguser.setType(user.getType());
                    }
                    if (user.getOrganization() != null) {
                        existinguser.setOrganization(user.getOrganization());
                    }
                    if (user.getPhoneNumber() != null) {
                        existinguser.setPhoneNumber(user.getPhoneNumber());
                    }
                    if (user.getCreatedDate() != null) {
                        existinguser.setCreatedDate(user.getCreatedDate());
                    }
                    if (user.getDob() != null) {
                        existinguser.setDob(user.getDob());
                    }
                    if (user.getMacAddress() != null) {
                        existinguser.setMacAddress(user.getMacAddress());
                    }
                    if (user.getPassword() != null) {
                        existinguser.setPassword(user.getPassword());
                    }

                    return existinguser;
                })
                .map(userRepository::save);
    }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        log.debug("Request to get all users");
        return userRepository.findAll(pageable);
    }

    /**
     * Get all the users with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    // public Page<User> findAllWithEagerRelationships(Pageable pageable) {
    // return userRepository.findAllWithEagerRelationships(pageable);
    // }

    /**
     * Get one user by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    // @Transactional(readOnly = true)
    // public Optional<User> findOne(Long id) {
    // log.debug("Request to get user : {}", id);
    // return userRepository.findOneWithEagerRelationships(id);
    // }

    /**
     * Delete the user by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete user : {}", id);
        userRepository.deleteById(id);
    }
}
