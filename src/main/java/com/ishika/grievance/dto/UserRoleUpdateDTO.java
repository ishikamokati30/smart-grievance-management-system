package com.ishika.grievance.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRoleUpdateDTO {

    @NotBlank(message = "Role is required")
    private String role;

    public UserRoleUpdateDTO() {
    }

    public UserRoleUpdateDTO(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
