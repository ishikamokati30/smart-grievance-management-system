package com.ishika.grievance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ishika.grievance.dto.DepartmentRequest;
import com.ishika.grievance.entity.Department;
import com.ishika.grievance.repository.DepartmentRepository;
import com.ishika.grievance.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/{id}")
    public Department getDepartment(
            @PathVariable Long id){

        return departmentRepository
                .findById(id)
                .orElse(null);
    }
    
    @PostMapping
    public String createDepartment(
            @RequestBody DepartmentRequest request) {

        return departmentService
                .createDepartment(request);
    }
}
