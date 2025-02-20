package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing employees.
 * Provides the endpoints for creating, reading, and updating employee data.
 */
@RestController
public class EmployeeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * Retrieves employee by employee id.
     *
     * @param employeeId The employee id of the employee to retrieve.
     * @return The employee with the associated employee id.
     */
    @GetMapping("/employee/{employeeId}")
    public Employee getEmployee(@PathVariable String employeeId) {
        LOG.debug("Received employee get request for id [{}]", employeeId);

        return employeeService.getEmployee(employeeId);
    }

    /**
     * Retrieves reporting structure for the employee with the associated employee id.
     *
     * @param employeeId The employee id of the employee for which we are retrieving reporting structure.
     * @return The reporting structure for the employee with the associated employee id.
     */
    @GetMapping("/reportingStructure/{employeeId}")
    public ReportingStructure getReportingStructure(@PathVariable String employeeId) {
        LOG.debug("Received reporting structure get request for employee with id [{}]", employeeId);

        return employeeService.getReportingStructure(employeeId);
    }

    /**
     * Creates a new employee.
     *
     * @param employee The employee object containing the new employee data.
     * @return The created employee.
     */
    @PostMapping("/employee")
    public Employee createEmployee(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.createEmployee(employee);
    }

    /**
     * Updates the employee with the associated employee id with the given employee data.
     *
     * @param employeeId The id of the employee to update.
     * @param employee The employee data with which to update the original employee.
     * @return The updated employee.
     */
    @PutMapping("/employee/{employeeId}")
    public Employee updateEmployee(@PathVariable String employeeId, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", employeeId, employee);

        employee.setEmployeeId(employeeId);
        return employeeService.updateEmployee(employee);
    }
}
