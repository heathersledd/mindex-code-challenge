package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Compensation} entities.
 * Extends MongoRepository to inherit basic CRUD operations.
 */
@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {

    Compensation findByEmployeeId(String employeeId);
}
