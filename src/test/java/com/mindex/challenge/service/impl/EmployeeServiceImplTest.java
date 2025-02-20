package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull(readEmployee);
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertNotNull(updatedEmployee);
        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    // Unit Tests

    @Test
    public void testInvalidEmployee() {
        // Test that attempting to get reporting structure with an invalid employee id throws an exception
        // Call with the service directly so we can pass an invalid id in
        assertThrows(RuntimeException.class, () ->
            employeeService.getReportingStructure("an-invalid-id"));
    }

    @Test
    public void testNoReports() {
        // Test computation of reports for an employee with no reports
        ReportingStructure reportingStructure = employeeService.getReportingStructure("b7839309-3348-463b-a7e3-5de1c168beb3");

        assertEquals(0, reportingStructure.getNumberOfReports());
    }

    @Test
    public void testReports() {
        // Test computation of reports for an employee with one level of reports
        ReportingStructure reportingStructure = employeeService.getReportingStructure("03aa1462-ffa9-4978-901b-7c001562cf6f");

        assertEquals(2, reportingStructure.getNumberOfReports());
    }

    @Test
    public void testComputeRecursiveReports() {
        // Test computation of reports for employee with multiple levels of reports
        ReportingStructure reportingStructure = employeeService.getReportingStructure("16a596ae-edd3-4847-99fe-c4518e82c86f");

        assertEquals(4, reportingStructure.getNumberOfReports());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
