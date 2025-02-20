package com.mindex.challenge.dao;

import com.mindex.challenge.data.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing {@link Employee} entities.
 * Extends MongoRepository to inherit basic CRUD operations.
 */
@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Employee findByEmployeeId(String employeeId);
}
