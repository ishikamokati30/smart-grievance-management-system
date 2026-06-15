package com.ishika.grievance.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ishika.grievance.entity.Complaint;
import com.ishika.grievance.repository.ComplaintRepository;

@Component
public class SlaScheduler {
	    @Autowired
	    private ComplaintRepository complaintRepository;

	    @Scheduled(fixedRate = 60000)
	    public void checkSla() {

	        List<Complaint> complaints =
	                complaintRepository.findAll();

	        for (Complaint complaint : complaints) {
	        	 if (complaint.getCreatedAt() == null) {
	        	        continue;
	        	    }

	            if ("OPEN".equals(complaint.getStatus())
	                    && complaint.getCreatedAt()
	                    .isBefore(
	                        LocalDateTime.now()
	                        .minusMinutes(1))) {

	                complaint.setStatus("ESCALATED");

	                complaintRepository.save(complaint);

	                System.out.println(
	                    "Escalated Complaint ID: "
	                    + complaint.getId());
	            }
	        }
	    }
}
