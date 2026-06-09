package com.ishika.grievance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.ishika.grievance.dto.ComplaintRequest;
import com.ishika.grievance.dto.UpdateStatusRequest;
import com.ishika.grievance.entity.Complaint;
import com.ishika.grievance.service.ComplaintService;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    public String createComplaint(
            @RequestBody ComplaintRequest request) {

        return complaintService.createComplaint(request);
    }
    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }
    

    @GetMapping("/{id}")
    public Complaint getComplaintById(
            @PathVariable Long id) {

        return complaintService.getComplaintById(id);
    }
    @PutMapping("/{id}")
    public String updateComplaintStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {

        return complaintService
                .updateComplaintStatus(id, request);
    }
    @DeleteMapping("/{id}")
    public String deleteComplaint(
            @PathVariable Long id) {

        return complaintService.deleteComplaint(id);
    }
    @PostMapping("/user/{userId}")
    public String createComplaintForUser(
            @PathVariable Long userId,
            @RequestBody ComplaintRequest request) {

        return complaintService
                .createComplaintForUser(userId, request);
}
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminOnly() {
        return "Welcome Admin";
    }
}