package com.munikiran.emplyeeDepartment.mapper;

import com.munikiran.emplyeeDepartment.dto.DepartmentDTO;
import com.munikiran.emplyeeDepartment.dto.EmployeeDTO;
import com.munikiran.emplyeeDepartment.entity.Department;
import com.munikiran.emplyeeDepartment.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentMapper {

    public static DepartmentDTO toDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setLocation(department.getLocation());

        List<EmployeeDTO> employeeDTOs = department.getEmployees()
                .stream()
                .map(DepartmentMapper::toEmployeeDTO)
                .collect(Collectors.toList());

        dto.setEmployees(employeeDTOs);
        return dto;
    }

    public static EmployeeDTO toEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        return dto;
    }
}
