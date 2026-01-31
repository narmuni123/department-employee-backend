package com.munikiran.emplyeeDepartment.repository;

import com.munikiran.emplyeeDepartment.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
}