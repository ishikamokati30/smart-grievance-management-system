package com.ishika.grievance.dto;

public class AdminDashboardResponseDTO {
    private long totalUsers;
    private long totalComplaints;
    private long openComplaints;
    private long resolvedComplaints;
    private long escalatedComplaints;
    private long totalDepartments;
    private long todayComplaints;
    private long todayResolved;

    public AdminDashboardResponseDTO() {
    }

    public AdminDashboardResponseDTO(long totalUsers, long totalComplaints, long openComplaints, long resolvedComplaints,
                                     long escalatedComplaints, long totalDepartments, long todayComplaints, long todayResolved) {
        this.totalUsers = totalUsers;
        this.totalComplaints = totalComplaints;
        this.openComplaints = openComplaints;
        this.resolvedComplaints = resolvedComplaints;
        this.escalatedComplaints = escalatedComplaints;
        this.totalDepartments = totalDepartments;
        this.todayComplaints = todayComplaints;
        this.todayResolved = todayResolved;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalComplaints() {
        return totalComplaints;
    }

    public void setTotalComplaints(long totalComplaints) {
        this.totalComplaints = totalComplaints;
    }

    public long getOpenComplaints() {
        return openComplaints;
    }

    public void setOpenComplaints(long openComplaints) {
        this.openComplaints = openComplaints;
    }

    public long getResolvedComplaints() {
        return resolvedComplaints;
    }

    public void setResolvedComplaints(long resolvedComplaints) {
        this.resolvedComplaints = resolvedComplaints;
    }

    public long getEscalatedComplaints() {
        return escalatedComplaints;
    }

    public void setEscalatedComplaints(long escalatedComplaints) {
        this.escalatedComplaints = escalatedComplaints;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public long getTodayComplaints() {
        return todayComplaints;
    }

    public void setTodayComplaints(long todayComplaints) {
        this.todayComplaints = todayComplaints;
    }

    public long getTodayResolved() {
        return todayResolved;
    }

    public void setTodayResolved(long todayResolved) {
        this.todayResolved = todayResolved;
    }
}
