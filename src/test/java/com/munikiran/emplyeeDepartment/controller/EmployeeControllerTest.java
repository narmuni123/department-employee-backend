package com.munikiran.emplyeeDepartment.controller;

import com.munikiran.emplyeeDepartment.common.ApiResponse;
import com.munikiran.emplyeeDepartment.common.constants.SuccessMessages;
import com.munikiran.emplyeeDepartment.entity.Employee;
import com.munikiran.emplyeeDepartment.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId("emp-1");
        testEmployee.setName("John Doe");
        testEmployee.setEmail("john@example.com");
        testEmployee.setPosition("Developer");
        testEmployee.setSalary(75000.0);
    }

    @Test
    void testAddEmployeeReturnsWrappedResponseWith201Status() {
        // Arrange
        String departmentId = "dept-1";
        Employee inputEmployee = new Employee();
        inputEmployee.setName("John Doe");
        inputEmployee.setEmail("john@example.com");
        inputEmployee.setPosition("Developer");
        inputEmployee.setSalary(75000.0);

        when(employeeService.addEmployee(eq(departmentId), any(Employee.class)))
                .thenReturn(testEmployee);

        // Act
        ResponseEntity<ApiResponse<Employee>> response = 
                employeeController.addEmployee(departmentId, inputEmployee);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        ApiResponse<Employee> apiResponse = response.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals(SuccessMessages.EMPLOYEE_CREATED, apiResponse.getMessage());
        assertNotNull(apiResponse.getData());
        assertEquals("emp-1", apiResponse.getData().getId());
        assertEquals("John Doe", apiResponse.getData().getName());
        assertEquals("john@example.com", apiResponse.getData().getEmail());
        assertEquals("Developer", apiResponse.getData().getPosition());
        assertEquals(75000.0, apiResponse.getData().getSalary());
        assertNull(apiResponse.getError());
        assertNotNull(apiResponse.getTimestamp());
    }

    @Test
    void testAddEmployeeUsesCorrectSuccessMessage() {
        // Arrange
        String departmentId = "dept-1";
        Employee inputEmployee = new Employee();
        
        when(employeeService.addEmployee(eq(departmentId), any(Employee.class)))
                .thenReturn(testEmployee);

        // Act
        ResponseEntity<ApiResponse<Employee>> response = 
                employeeController.addEmployee(departmentId, inputEmployee);

        // Assert
        assertEquals(SuccessMessages.EMPLOYEE_CREATED, response.getBody().getMessage());
    }

    @Test
    void testAddEmployeePreservesEmployeeData() {
        // Arrange
        String departmentId = "dept-1";
        Employee inputEmployee = new Employee();
        inputEmployee.setName("Jane Smith");
        inputEmployee.setEmail("jane@example.com");
        inputEmployee.setPosition("Manager");
        inputEmployee.setSalary(90000.0);

        Employee savedEmployee = new Employee();
        savedEmployee.setId("emp-2");
        savedEmployee.setName("Jane Smith");
        savedEmployee.setEmail("jane@example.com");
        savedEmployee.setPosition("Manager");
        savedEmployee.setSalary(90000.0);

        when(employeeService.addEmployee(eq(departmentId), any(Employee.class)))
                .thenReturn(savedEmployee);

        // Act
        ResponseEntity<ApiResponse<Employee>> response = 
                employeeController.addEmployee(departmentId, inputEmployee);

        // Assert
        Employee returnedEmployee = response.getBody().getData();
        assertEquals("emp-2", returnedEmployee.getId());
        assertEquals("Jane Smith", returnedEmployee.getName());
        assertEquals("jane@example.com", returnedEmployee.getEmail());
        assertEquals("Manager", returnedEmployee.getPosition());
        assertEquals(90000.0, returnedEmployee.getSalary());
    }
}
