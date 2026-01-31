package com.munikiran.emplyeeDepartment.service;

import com.munikiran.emplyeeDepartment.entity.Department;
import com.munikiran.emplyeeDepartment.entity.Employee;
import com.munikiran.emplyeeDepartment.exception.ResourceNotFoundException;
import com.munikiran.emplyeeDepartment.repository.DepartmentRepository;
import com.munikiran.emplyeeDepartment.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(String deptId) {
        if (!departmentRepo.existsById(deptId)) {
            throw new ResourceNotFoundException("Department not found: " + deptId);
        }
        return employeeRepo.findByDepartmentId(deptId);
    }

    @Transactional
    public Employee addEmployee(String deptId, Employee employee) {

        Department dept = departmentRepo.findById(deptId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found: " + deptId));

        employee.setDepartment(dept);
        dept.getEmployees().add(employee);

        departmentRepo.save(dept);

        return employee;
    }

    @Transactional
    public void deleteEmployee(String deptId, String empId) {

        Department dept = departmentRepo.findById(deptId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found: " + deptId));

        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found: " + empId));

        dept.getEmployees().remove(emp);
        employeeRepo.delete(emp);
    }
}
