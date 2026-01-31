package com.munikiran.emplyeeDepartment.controller;

import com.munikiran.emplyeeDepartment.entity.Department;
import com.munikiran.emplyeeDepartment.entity.Employee;
import com.munikiran.emplyeeDepartment.repository.DepartmentRepository;
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
    private final DepartmentRepository departmentRepo;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(service.getAllEmployees());
    }

    @PostMapping("/departments/{id}/employees")
    public ResponseEntity<Employee> addEmployee(
            @PathVariable String id,
            @RequestBody Employee emp) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.addEmployee(id, emp));
    }

    @DeleteMapping("/departments/{deptId}/employees/{empId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable String deptId,
            @PathVariable String empId) {

        service.deleteEmployee(deptId, empId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> create(@RequestBody Department d) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentRepo.save(d));
    }
}
