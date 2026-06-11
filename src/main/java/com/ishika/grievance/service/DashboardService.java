package com.ishika.grievance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishika.grievance.dto.DashboardResponse;
import com.ishika.grievance.repository.ComplaintRepository;

@Service
public class DashboardService {
	    @Autowired
	    private ComplaintRepository complaintRepository;

	    public DashboardResponse getStats() {

	        long total =
	                complaintRepository.count();

	        long open =
	                complaintRepository
	                .countByStatus("OPEN");

	        long resolved =
	                complaintRepository
	                .countByStatus("RESOLVED");

	        return new DashboardResponse(
	                total,
	                open,
	                resolved);
	    }
}
