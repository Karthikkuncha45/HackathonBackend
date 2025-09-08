package com.nec.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "farmers")
public class Farmer {

    @Id
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role = "FARMER";  // Default role

    // Loan info
    private Double loanAmount = 0.0;
    private String loanStatus = "NONE"; // NONE, PENDING, APPROVED, REJECTED

    // Repayment info
    private Double repaidAmount = 0.0;

    // Subsidy info
    private Double subsidyAmount = 0.0;
    private String subsidyStatus = "NONE"; // NONE, APPLIED, APPROVED, REJECTED

    // --- Getters & Setters ---
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }
    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanStatus() {
        return loanStatus;
    }
    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public Double getRepaidAmount() {
        return repaidAmount;
    }
    public void setRepaidAmount(Double repaidAmount) {
        this.repaidAmount = repaidAmount;
    }

    public Double getSubsidyAmount() {
        return subsidyAmount;
    }
    public void setSubsidyAmount(Double subsidyAmount) {
        this.subsidyAmount = subsidyAmount;
    }

    public String getSubsidyStatus() {
        return subsidyStatus;
    }
    public void setSubsidyStatus(String subsidyStatus) {
        this.subsidyStatus = subsidyStatus;
    }
}
