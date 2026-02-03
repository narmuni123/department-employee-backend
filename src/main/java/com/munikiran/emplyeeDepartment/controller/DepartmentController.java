package com.munikiran.emplyeeDepartment.controller;

import com.munikiran.emplyeeDepartment.common.ApiResponse;
import com.munikiran.emplyeeDepartment.common.constants.SuccessMessages;
import com.munikiran.emplyeeDepartment.dto.DepartmentDTO;
import com.munikiran.emplyeeDepartment.dto.EmployeeDTO;
import com.munikiran.emplyeeDepartment.entity.Department;
import com.munikiran.emplyeeDepartment.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDTO>>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        ApiResponse<List<DepartmentDTO>> response = ApiResponse.success(
            SuccessMessages.DEPARTMENTS_RETRIEVED,
            departments
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{deptId}/employees")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getEmployees(@PathVariable String deptId) {
        List<EmployeeDTO> employees = departmentService.getEmployeesByDepartment(deptId);
        ApiResponse<List<EmployeeDTO>> response = ApiResponse.success(
            SuccessMessages.EMPLOYEES_RETRIEVED,
            employees
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees-map")
    public ResponseEntity<ApiResponse<Map<String, List<EmployeeDTO>>>> getEmployeesMap() {
        Map<String, List<EmployeeDTO>> employeesMap = departmentService.getEmployeesGroupedByDepartment();
        ApiResponse<Map<String, List<EmployeeDTO>>> response = ApiResponse.success(
            SuccessMessages.EMPLOYEES_RETRIEVED,
            employeesMap
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentDTO>> createDepartment(@RequestBody Department department) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(department);
        ApiResponse<DepartmentDTO> response = ApiResponse.success(
            SuccessMessages.DEPARTMENT_CREATED,
            createdDepartment
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> updateDepartment(
            @PathVariable String id,
            @RequestBody Department department) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(id, department);
        ApiResponse<DepartmentDTO> response = ApiResponse.success(
            SuccessMessages.DEPARTMENT_UPDATED,
            updatedDepartment
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        ApiResponse<Void> response = ApiResponse.success(
            SuccessMessages.DEPARTMENT_DELETED,
            null
        );
        return ResponseEntity.ok(response);
    }
}
