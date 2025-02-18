package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implements business logic for managing employee data.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployee(String employeeId) {
        LOG.debug("Getting employee with id [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        return employee;
    }

    @Override
    public ReportingStructure getReportingStructure(String employeeId) {
        LOG.debug("Creating reporting structure for employee with id [{}]", employeeId);

        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }

        return new ReportingStructure(employee, computeNumberOfReports(employeeId));
    }

    private int computeNumberOfReports(String employeeId) {
        // Base case, employee has no reports
        // Return, effectively ending recursive trail
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        List<Employee> directReports = employee.getDirectReports();
        if (directReports.isEmpty()) {
            return 0;
        }

        // Recursively add number of direct reports for subsequent report to the rolling count
        int numDirectReports = directReports.size();
        for (Employee report : directReports) {
            numDirectReports += computeNumberOfReports(report.getEmployeeId());
        }

        return numDirectReports;
    }
}
