package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

/**
 * Defines the contract for managing employee data.
 */
public interface EmployeeService {

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    Employee getEmployee(String employeeId);

    ReportingStructure getReportingStructure(String employeeId);
}
