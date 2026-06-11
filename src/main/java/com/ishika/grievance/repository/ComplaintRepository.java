package com.ishika.grievance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishika.grievance.entity.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint,Long>{
	long countByStatus(String status);
}
