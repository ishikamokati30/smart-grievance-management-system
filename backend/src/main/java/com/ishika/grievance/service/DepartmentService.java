package com.ishika.grievance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishika.grievance.dto.DepartmentRequest;
import com.ishika.grievance.entity.Department;
import com.ishika.grievance.repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public String createDepartment(
            DepartmentRequest request) {

        Department department =
                new Department();

        department.setName(
                request.getName());

        department.setEmail(
                request.getEmail());

        departmentRepository.save(
                department);

        return "Department Created Successfully";
    }
}
