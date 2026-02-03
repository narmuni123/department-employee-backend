package com.munikiran.emplyeeDepartment.controller;

import com.munikiran.emplyeeDepartment.common.ApiRequest;
import com.munikiran.emplyeeDepartment.common.ApiResponse;
import com.munikiran.emplyeeDepartment.common.constants.SuccessMessages;
import com.munikiran.emplyeeDepartment.entity.Employee;
import com.munikiran.emplyeeDepartment.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees() {
        List<Employee> employees = service.getAllEmployees();
        ApiResponse<List<Employee>> response = ApiResponse.success(
            SuccessMessages.EMPLOYEES_RETRIEVED,
            employees
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/departments/{id}/employees")
    public ResponseEntity<ApiResponse<Employee>> addEmployee(
            @PathVariable String id,
            @RequestBody ApiRequest<Employee> request) {

        Employee employee = service.addEmployee(id, request.getPayload());
        ApiResponse<Employee> response = ApiResponse.success(
            SuccessMessages.EMPLOYEE_CREATED,
            employee
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/departments/{deptId}/employees/{empId}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(
            @PathVariable String deptId,
            @PathVariable String empId) {

        service.deleteEmployee(deptId, empId);
        ApiResponse<Void> response = ApiResponse.success(
            SuccessMessages.EMPLOYEE_DELETED,
            null
        );
        return ResponseEntity.ok(response);
    }
}
