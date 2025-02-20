package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Test class for <code>CompensationServiceImpl</code>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationUrl;
    private String compensationEmployeeIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationEmployeeIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Jane");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer II");

        // Create checks
        // Create employee to be associated with the compensation
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(createdEmployee);
        assertNotNull(createdEmployee.getEmployeeId());

        // Create compensation to be associated with the created employee
        Compensation testCompensation = new Compensation();
        testCompensation.setEffectiveDate(LocalDate.now());
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setSalary(new BigDecimal(100000));

        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        assertNotNull(createdCompensation);

        // Assert that create compensation endpoint functions as expected
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationEmployeeIdUrl, Compensation.class, createdCompensation.getEmployeeId()).getBody();
        assertNotNull(readCompensation);

        // Assert that read compensation endpoint functions as expected
        assertCompensationEquivalence(createdCompensation, readCompensation);

        // Assert that employee and compensation are indeed associated
        assertEquals(createdEmployee.getEmployeeId(), readCompensation.getEmployeeId());

        // Assert that retrieving compensation via employee id returns expected value
        assertEquals(readCompensation.getSalary(), new BigDecimal(100000));
    }

    // Unit Tests

    @Test
    public void validateEmployeeIdOnCompensationCreate() {
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId("an-invalid-id");
        testCompensation.setSalary(new BigDecimal(100000));
        testCompensation.setEffectiveDate(LocalDate.now());

        // Assert that exception is thrown when invalid employee id is passed on compensation create
        assertThrows(IllegalArgumentException.class, () -> compensationService.createCompensation(testCompensation));
    }

    @Test
    public void validateSalaryOnCompensationCreate() {
        Employee testEmployee = new Employee();
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(createdEmployee);

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setSalary(new BigDecimal(-100000));
        testCompensation.setEffectiveDate(LocalDate.now());

        // Assert that exception is thrown when negative salary is passed on compensation create
        assertThrows(IllegalArgumentException.class, () -> compensationService.createCompensation(testCompensation));
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
    }
}
