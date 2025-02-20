package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing compensations.
 * Provides the endpoints for creating and reading compensation data.
 */
@RestController
public class CompensationController {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    /**
     * Retrieves compensation by employee id.
     *
     * @param employeeId The employee id of the employee associated with the compensation to retrieve.
     * @return The compensation with the associated employee id
     */
    @GetMapping("/compensation/{employeeId}")
    public Compensation getCompensation(@PathVariable String employeeId) {
        LOG.debug("Received compensation get request for employee with id [{}]", employeeId);

        return compensationService.getCompensation(employeeId);
    }

    /**
     * Creates a new compensation.
     *
     * @param compensation The compensation object containing the new compensation data.
     * @return The created compensation.
     */
    @PostMapping("/compensation")
    public Compensation createCompensation(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        return compensationService.createCompensation(compensation);
    }
}
