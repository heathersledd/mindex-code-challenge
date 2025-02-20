package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

/**
 * Defines the contract for managing employee compensation.
 */
public interface CompensationService {

    Compensation createCompensation(Compensation compensation);

    Compensation getCompensation(String employeeId);
}
