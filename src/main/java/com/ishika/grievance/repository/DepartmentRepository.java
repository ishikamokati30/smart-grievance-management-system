package com.ishika.grievance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishika.grievance.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department,Long> {

}
