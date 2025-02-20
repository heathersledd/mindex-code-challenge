package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implements business logic for managing employee compensation data.
 */
@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation createCompensation(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        // If we can assume that Compensation can not be created unless the employee id is associated with an existing employee, validate the employee id
        try {
            employeeService.getEmployee(compensation.getEmployeeId());
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
        }

        // Validate that salary input is a positive number
        if (compensation.getSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salary cannot be a negative number.");
        }

        compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public Compensation getCompensation(String employeeId) {
        LOG.debug("Getting compensation for employee with employeeId [{}]", employeeId);

        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
        if (compensation == null) {
            throw new RuntimeException("Compensation data does not exist for employeeId: " + employeeId);
        }

        return compensation;
    }
}
