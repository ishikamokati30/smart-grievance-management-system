package com.ishika.grievance.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
	@Table(name = "departments")
	public class Department {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;

	    private String email;

	    @JsonManagedReference
	    @OneToMany(mappedBy = "department")
	    private List<Complaint> complaints;

	    public Department() {
	    }

	    public Department(Long id, String name, String email) {
	        this.id = id;
	        this.name = name;
	        this.email = email;
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public List<Complaint> getComplaints() {
	        return complaints;
	    }

	    public void setComplaints(List<Complaint> complaints) {
	        this.complaints = complaints;
	    }
	}

