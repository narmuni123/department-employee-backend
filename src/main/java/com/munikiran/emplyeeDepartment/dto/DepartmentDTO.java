package com.munikiran.emplyeeDepartment.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDTO {
    private String id;
    private String name;
    private String location;
    private List<EmployeeDTO> employees;

}
