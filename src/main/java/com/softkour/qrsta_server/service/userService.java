package com.softkour.qrsta_server.service;

import com.softkour.qrsta_server.entity.User;
import com.softkour.qrsta_server.repo.EmployeeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.softkour.qrsta_server.User.Employee}.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Save a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    public User save(User employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Update a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    public User update(User employee) {
        log.debug("Request to update Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    /**
     * Partially update a employee.
     *
     * @param employee the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<User> partialUpdate(User employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
                .findById(employee.getId())
                .map(existingEmployee -> {
                    if (employee.getId() != null) {
                        existingEmployee.setId(employee.getId());
                    }
                    if (employee.getName() != null) {
                        existingEmployee.setName(employee.getName());
                    }
                    if (employee.getType() != null) {
                        existingEmployee.setType(employee.getType());
                    }
                    if (employee.getOrganization() != null) {
                        existingEmployee.setOrganization(employee.getOrganization());
                    }
                    if (employee.getPhoneNumber() != null) {
                        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
                    }
                    if (employee.getCreatedDate() != null) {
                        existingEmployee.setCreatedDate(employee.getCreatedDate());
                    }
                    if (employee.getDob() != null) {
                        existingEmployee.setDob(employee.getDob());
                    }
                    if (employee.getMacAddress() != null) {
                        existingEmployee.setMacAddress(employee.getMacAddress());
                    }
                    if (employee.getPassword() != null) {
                        existingEmployee.setPassword(employee.getPassword());
                    }

                    return existingEmployee;
                })
                .map(employeeRepository::save);
    }

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable);
    }

    /**
     * Get all the employees with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<User> findAllWithEagerRelationships(Pageable pageable) {
        return employeeRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<User> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
