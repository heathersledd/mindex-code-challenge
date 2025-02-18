package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implements business logic for managing employee compensation data.
 */
@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation createCompensation(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public Compensation getCompensation(String employeeId) {
        LOG.debug("Getting compensation with employeeId [{}]", employeeId);

        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
        if (compensation == null) {
            throw new RuntimeException("Compensation data does not exist for employeeId: " + employeeId);
        }

        return compensation;
    }
}
