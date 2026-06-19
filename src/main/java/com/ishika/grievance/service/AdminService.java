package com.ishika.grievance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ishika.grievance.dto.*;
import com.ishika.grievance.entity.Complaint;
import com.ishika.grievance.entity.Department;
import com.ishika.grievance.entity.User;
import com.ishika.grievance.enums.Role;
import com.ishika.grievance.exception.*;
import com.ishika.grievance.repository.ComplaintRepository;
import com.ishika.grievance.repository.DepartmentRepository;
import com.ishika.grievance.repository.UserRepository;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AuditLogService auditLogService;

    // ==========================================
    // USER MANAGEMENT
    // ==========================================

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsers(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        if (email != null && !email.trim().isEmpty()) {
            userPage = userRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        return userPage.map(this::mapToUserResponseDTO);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return mapToUserResponseDTO(user);
    }

    public UserResponseDTO updateUserRole(Long id, UserRoleUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        Role newRole;
        try {
            newRole = Role.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role value: " + dto.getRole() + ". Allowed roles: USER, AGENT, SUPERVISOR, ADMIN");
        }

        Role oldRole = user.getRole();
        user.setRole(newRole);
        userRepository.save(user);

        auditLogService.log("Changed role of user " + user.getEmail() + " (ID: " + id + ") from " + oldRole + " to " + newRole);

        return mapToUserResponseDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);
        auditLogService.log("Deleted user with email " + user.getEmail() + " (ID: " + id + ")");
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    // ==========================================
    // COMPLAINT MANAGEMENT
    // ==========================================

    @Transactional(readOnly = true)
    public Page<ComplaintResponseDTO> getComplaints(String status, Long departmentId, String priority, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String filterStatus = (status != null && !status.trim().isEmpty()) ? status : null;
        String filterPriority = (priority != null && !priority.trim().isEmpty()) ? priority : null;

        Page<Complaint> complaintPage = complaintRepository.findByFilters(filterStatus, departmentId, filterPriority, pageable);
        return complaintPage.map(this::mapToComplaintResponseDTO);
    }

    @Transactional(readOnly = true)
    public ComplaintResponseDTO getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with ID: " + id));
        return mapToComplaintResponseDTO(complaint);
    }

    public ComplaintResponseDTO updateComplaintStatus(Long id, ComplaintStatusUpdateDTO dto) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with ID: " + id));

        String oldStatus = complaint.getStatus();
        String newStatus = dto.getStatus().toUpperCase();

        // Simple validation check: status should be one of the typical values
        if (!newStatus.equals("OPEN") && !newStatus.equals("IN_PROGRESS") && !newStatus.equals("RESOLVED") && !newStatus.equals("ESCALATED")) {
            throw new InvalidStatusException("Invalid status: " + dto.getStatus() + ". Allowed statuses: OPEN, IN_PROGRESS, RESOLVED, ESCALATED");
        }

        complaint.setStatus(newStatus);
        complaintRepository.save(complaint);

        auditLogService.log("Updated complaint " + id + " status from " + oldStatus + " to " + newStatus);

        return mapToComplaintResponseDTO(complaint);
    }

    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with ID: " + id));

        complaintRepository.delete(complaint);
        auditLogService.log("Deleted complaint with ID: " + id);
    }

    @Transactional(readOnly = true)
    public ComplaintStatsResponseDTO getComplaintStatistics() {
        List<Complaint> complaints = complaintRepository.findAll();
        long total = complaints.size();

        Map<String, Long> statusCounts = new HashMap<>();
        Map<String, Long> priorityCounts = new HashMap<>();

        for (Complaint c : complaints) {
            String status = c.getStatus() != null ? c.getStatus().toUpperCase() : "UNKNOWN";
            statusCounts.put(status, statusCounts.getOrDefault(status, 0L) + 1);

            String priority = c.getPriority() != null ? c.getPriority().toUpperCase() : "UNKNOWN";
            priorityCounts.put(priority, priorityCounts.getOrDefault(priority, 0L) + 1);
        }

        return new ComplaintStatsResponseDTO(total, statusCounts, priorityCounts);
    }

    private ComplaintResponseDTO mapToComplaintResponseDTO(Complaint complaint) {
        Long userId = complaint.getUser() != null ? complaint.getUser().getId() : null;
        String userName = complaint.getUser() != null ? complaint.getUser().getName() : null;
        String userEmail = complaint.getUser() != null ? complaint.getUser().getEmail() : null;
        Long departmentId = complaint.getDepartment() != null ? complaint.getDepartment().getId() : null;
        String departmentName = complaint.getDepartment() != null ? complaint.getDepartment().getName() : null;

        return new ComplaintResponseDTO(
                complaint.getId(),
                complaint.getTitle(),
                complaint.getDescription(),
                complaint.getCategory(),
                complaint.getPriority(),
                complaint.getStatus(),
                complaint.getCreatedAt(),
                userId,
                userName,
                userEmail,
                departmentId,
                departmentName
        );
    }

    // ==========================================
    // DEPARTMENT MANAGEMENT
    // ==========================================

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDepartmentDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department dept = new Department();
        dept.setName(dto.getName());
        dept.setEmail(dto.getEmail());
        departmentRepository.save(dept);

        auditLogService.log("Created department " + dept.getName() + " (ID: " + dept.getId() + ") with email " + dept.getEmail());

        return mapToDepartmentDTO(dept);
    }

    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + id));

        dept.setName(dto.getName());
        dept.setEmail(dto.getEmail());
        departmentRepository.save(dept);

        auditLogService.log("Updated department " + id + " details to Name: " + dept.getName() + ", Email: " + dept.getEmail());

        return mapToDepartmentDTO(dept);
    }

    public void deleteDepartment(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + id));

        departmentRepository.delete(dept);
        auditLogService.log("Deleted department " + dept.getName() + " (ID: " + id + ")");
    }

    @Transactional(readOnly = true)
    public List<DepartmentAnalyticsResponseDTO> getDepartmentAnalytics() {
        return departmentRepository.findAll().stream().map(dept -> {
            List<Complaint> complaints = dept.getComplaints();
            long total = complaints != null ? complaints.size() : 0L;
            long resolved = complaints != null ? complaints.stream()
                    .filter(c -> "RESOLVED".equalsIgnoreCase(c.getStatus()))
                    .count() : 0L;
            long pending = total - resolved;

            return new DepartmentAnalyticsResponseDTO(
                    dept.getId(),
                    dept.getName(),
                    total,
                    resolved,
                    pending
            );
        }).collect(Collectors.toList());
    }

    private DepartmentDTO mapToDepartmentDTO(Department dept) {
        return new DepartmentDTO(dept.getId(), dept.getName(), dept.getEmail());
    }

    // ==========================================
    // ADMIN DASHBOARD
    // ==========================================

    @Transactional(readOnly = true)
    public AdminDashboardResponseDTO getDashboard() {
        long totalUsers = userRepository.count();
        long totalComplaints = complaintRepository.count();
        long openComplaints = complaintRepository.countByStatus("OPEN");
        long resolvedComplaints = complaintRepository.countByStatus("RESOLVED");
        long escalatedComplaints = complaintRepository.countByStatus("ESCALATED");
        long totalDepartments = departmentRepository.count();

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        long todayComplaints = complaintRepository.countByCreatedAtAfter(startOfDay);
        long todayResolved = complaintRepository.countByStatusAndCreatedAtAfter("RESOLVED", startOfDay);

        return new AdminDashboardResponseDTO(
                totalUsers,
                totalComplaints,
                openComplaints,
                resolvedComplaints,
                escalatedComplaints,
                totalDepartments,
                todayComplaints,
                todayResolved
        );
    }
}
