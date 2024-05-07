package edu.ncsu.csc.iTrust2.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.repositories.PatientAdvocateRepository;

/**
 * Service class for interacting with Patient model, performing CRUD tasks with
 * database.
 *
 * @author John Gobran (jmgobran)
 * @param <T>
 *            Type of user
 *
 */
@Component
@Transactional
public class PatientAdvocateService <T extends User> extends UserService<PatientAdvocate> {

    /**
     * Repository for CRUD operations
     */
    @Autowired
    private PatientAdvocateRepository<PatientAdvocate> repository;

    @Override
    protected JpaRepository<PatientAdvocate, String> getRepository () {
        return repository;
    }

}
