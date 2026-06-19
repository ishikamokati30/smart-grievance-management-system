package com.ishika.grievance.dto;

import jakarta.validation.constraints.NotBlank;

public class ComplaintStatusUpdateDTO {

    @NotBlank(message = "Status is required")
    private String status;

    public ComplaintStatusUpdateDTO() {
    }

    public ComplaintStatusUpdateDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
