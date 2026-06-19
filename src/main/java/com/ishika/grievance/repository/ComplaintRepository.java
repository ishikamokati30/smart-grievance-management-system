package com.ishika.grievance.repository;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ishika.grievance.entity.Complaint;
import com.ishika.grievance.entity.Department;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    
    long countByStatus(String status);
    
    Page<Complaint> findByStatus(String status, Pageable pageable);
    
    Page<Complaint> findByDepartment(Department department, Pageable pageable);
    
    Page<Complaint> findByPriority(String priority, Pageable pageable);
    
    long countByCreatedAtAfter(LocalDateTime dateTime);
    
    long countByStatusAndCreatedAtAfter(String status, LocalDateTime dateTime);

    @Query("SELECT c FROM Complaint c WHERE " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:departmentId IS NULL OR c.department.id = :departmentId) AND " +
           "(:priority IS NULL OR c.priority = :priority)")
    Page<Complaint> findByFilters(
        @Param("status") String status,
        @Param("departmentId") Long departmentId,
        @Param("priority") String priority,
        Pageable pageable
    );
}
