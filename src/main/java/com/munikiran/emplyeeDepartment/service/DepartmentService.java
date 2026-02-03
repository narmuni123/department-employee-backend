package com.munikiran.emplyeeDepartment.service;

import com.munikiran.emplyeeDepartment.common.constants.ErrorMessages;
import com.munikiran.emplyeeDepartment.dto.DepartmentDTO;
import com.munikiran.emplyeeDepartment.dto.EmployeeDTO;
import com.munikiran.emplyeeDepartment.entity.Department;
import com.munikiran.emplyeeDepartment.exception.ResourceNotFoundException;
import com.munikiran.emplyeeDepartment.mapper.DepartmentMapper;
import com.munikiran.emplyeeDepartment.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(String deptId) {
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND + deptId));

        return department.getEmployees()
                .stream()
                .map(DepartmentMapper::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND + deptId));

        department.getEmployees()
                .removeIf(emp -> emp.getId().equals(empId));
    }

    @Transactional
    public DepartmentDTO createDepartment(Department department) {
        Department savedDepartment = departmentRepository.save(department);
        return DepartmentMapper.toDTO(savedDepartment);
    }

    @Transactional
    public DepartmentDTO updateDepartment(String id, Department department) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND + id));
        
        existingDepartment.setName(department.getName());
        existingDepartment.setLocation(department.getLocation());
        
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return DepartmentMapper.toDTO(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND + id));
        departmentRepository.delete(department);
    }
}
