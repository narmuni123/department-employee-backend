package com.munikiran.emplyeeDepartment.service;

import com.munikiran.emplyeeDepartment.dto.DepartmentDTO;
import com.munikiran.emplyeeDepartment.dto.EmployeeDTO;
import com.munikiran.emplyeeDepartment.entity.Department;
import com.munikiran.emplyeeDepartment.mapper.DepartmentMapper;
import com.munikiran.emplyeeDepartment.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getEmployeesByDepartment(String deptId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return department.getEmployees()
                .stream()
                .map(DepartmentMapper::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    public Map<String, List<EmployeeDTO>> getEmployeesGroupedByDepartment() {
        return departmentRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Department::getId,
                        dept -> dept.getEmployees()
                                .stream()
                                .map(DepartmentMapper::toEmployeeDTO)
                                .collect(Collectors.toList())
                ));
    }

    @Transactional
    public void deleteEmployee(String deptId, String empId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.getEmployees()
                .removeIf(emp -> emp.getId().equals(empId));
    }
}
