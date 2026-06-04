package com.ishika.grievance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishika.grievance.dto.ComplaintRequest;
import com.ishika.grievance.dto.UpdateStatusRequest;
import com.ishika.grievance.entity.Complaint;
import com.ishika.grievance.repository.ComplaintRepository;

@Service
public class ComplaintService {
	  @Autowired
	    private ComplaintRepository complaintRepository;

	    public String createComplaint(
	            ComplaintRequest request) {

	        Complaint complaint = new Complaint();

	        complaint.setTitle(request.getTitle());
	        complaint.setDescription(request.getDescription());
	        complaint.setCategory(request.getCategory());
	        complaint.setPriority(request.getPriority());

	        complaint.setStatus("OPEN");

	        complaintRepository.save(complaint);

	        return "Complaint Created Successfully";
	    }
	    
	    public List<Complaint> getAllComplaints() {
	        return complaintRepository.findAll();
	    }
	    
	    public Complaint getComplaintById(Long id) {

	        return complaintRepository.findById(id)
	                .orElseThrow(() ->
	                    new RuntimeException("Complaint Not Found"));
	    }
	    public String updateComplaintStatus(
	            Long id,
	            UpdateStatusRequest request) {

	        Complaint complaint =
	                complaintRepository.findById(id)
	                .orElseThrow(() ->
	                 new RuntimeException("Complaint Not Found"));

	        complaint.setStatus(request.getStatus());

	        complaintRepository.save(complaint);

	        return "Status Updated Successfully";
	    }
	    public String deleteComplaint(Long id) {

	        complaintRepository.deleteById(id);

	        return "Complaint Deleted Successfully";
	    }
}
