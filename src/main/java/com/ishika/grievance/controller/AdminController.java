package com.ishika.grievance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ishika.grievance.dto.*;
import com.ishika.grievance.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Admin Management System", description = "Enterprise management endpoints for users, complaints, departments, and platform statistics.")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ==========================================
    // USER MANAGEMENT
    // ==========================================

    @GetMapping("/users")
    @Operation(summary = "Get Users", description = "Retrieve a paginated list of registered users. Can filter results by email.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users page"),
        @ApiResponse(responseCode = "403", description = "Forbidden: Access restricted to ADMIN")
    })
    public Page<UserResponseDTO> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getUsers(email, page, size);
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get User by ID", description = "Retrieve details for a specific user by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User details found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return adminService.getUserById(id);
    }

    @PutMapping("/users/{id}/role")
    @Operation(summary = "Update User Role", description = "Change a user's access role (e.g. USER, AGENT, SUPERVISOR, ADMIN).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User role updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid role value provided"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDTO updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateDTO dto) {
        return adminService.updateUserRole(id, dto);
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete User", description = "Permanently remove a user from the platform.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // COMPLAINT MANAGEMENT
    // ==========================================

    @GetMapping("/complaints")
    @Operation(summary = "Get Complaints", description = "Retrieve a paginated list of complaints. Can filter by status, department ID, or priority.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved complaints page")
    })
    public Page<ComplaintResponseDTO> getComplaints(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getComplaints(status, departmentId, priority, page, size);
    }

    @GetMapping("/complaints/{id}")
    @Operation(summary = "Get Complaint by ID", description = "Retrieve specific details of a complaint by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Complaint found"),
        @ApiResponse(responseCode = "404", description = "Complaint not found")
    })
    public ComplaintResponseDTO getComplaintById(@PathVariable Long id) {
        return adminService.getComplaintById(id);
    }

    @PutMapping("/complaints/{id}/status")
    @Operation(summary = "Update Complaint Status", description = "Update the resolution status of a complaint (e.g. OPEN, IN_PROGRESS, RESOLVED, ESCALATED).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Complaint status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status provided"),
        @ApiResponse(responseCode = "404", description = "Complaint not found")
    })
    public ComplaintResponseDTO updateComplaintStatus(
            @PathVariable Long id,
            @Valid @RequestBody ComplaintStatusUpdateDTO dto) {
        return adminService.updateComplaintStatus(id, dto);
    }

    @DeleteMapping("/complaints/{id}")
    @Operation(summary = "Delete Complaint", description = "Permanently remove a complaint from the platform.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Complaint deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Complaint not found")
    })
    public ResponseEntity<Map<String, String>> deleteComplaint(@PathVariable Long id) {
        adminService.deleteComplaint(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Complaint deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/complaints/statistics")
    @Operation(summary = "Get Complaint Statistics", description = "Retrieve aggregate statistics for complaints, grouped by status and priority.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ComplaintStatsResponseDTO getComplaintStatistics() {
        return adminService.getComplaintStatistics();
    }

    // ==========================================
    // DEPARTMENT MANAGEMENT
    // ==========================================

    @GetMapping("/departments")
    @Operation(summary = "Get All Departments", description = "Retrieve a list of all active departments.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved departments list")
    })
    public List<DepartmentDTO> getAllDepartments() {
        return adminService.getAllDepartments();
    }

    @PostMapping("/departments")
    @Operation(summary = "Create Department", description = "Add a new department to the platform.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Department created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or department name already exists")
    })
    public DepartmentDTO createDepartment(@Valid @RequestBody DepartmentDTO dto) {
        return adminService.createDepartment(dto);
    }

    @PutMapping("/departments/{id}")
    @Operation(summary = "Update Department", description = "Update details (e.g. name, email) of an existing department.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Department updated successfully"),
        @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public DepartmentDTO updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentDTO dto) {
        return adminService.updateDepartment(id, dto);
    }

    @DeleteMapping("/departments/{id}")
    @Operation(summary = "Delete Department", description = "Delete an existing department.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Department deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<Map<String, String>> deleteDepartment(@PathVariable Long id) {
        adminService.deleteDepartment(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Department deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/departments/analytics")
    @Operation(summary = "Get Department Analytics", description = "Get counts of total, resolved, and pending complaints for each department.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully")
    })
    public List<DepartmentAnalyticsResponseDTO> getDepartmentAnalytics() {
        return adminService.getDepartmentAnalytics();
    }

    // ==========================================
    // ADMIN DASHBOARD
    // ==========================================

    @GetMapping("/dashboard")
    @Operation(summary = "Get Admin Dashboard Metrics", description = "Retrieve summary metrics including total users, complaints by status, and daily totals.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dashboard metrics retrieved successfully")
    })
    public AdminDashboardResponseDTO getDashboard() {
        return adminService.getDashboard();
    }
}
