package com.munikiran.emplyeeDepartment.controller;

import com.munikiran.emplyeeDepartment.dto.DepartmentDTO;
import com.munikiran.emplyeeDepartment.dto.EmployeeDTO;
import com.munikiran.emplyeeDepartment.service.DepartmentService;
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
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{deptId}/employees")
    public List<EmployeeDTO> getEmployees(@PathVariable String deptId) {
        return departmentService.getEmployeesByDepartment(deptId);
    }

    @GetMapping("/employees-map")
    public Map<String, List<EmployeeDTO>> getEmployeesMap() {
        return departmentService.getEmployeesGroupedByDepartment();
    }
}
