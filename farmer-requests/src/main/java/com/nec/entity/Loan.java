package com.nec.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String farmerEmail;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    private String purpose;

    // Extra fields
    private String aadharNumber;
    private String rationCardNumber;
    private String bankAccountNumber;

    private String passbookFile;
    private String landProofFile;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFarmerEmail() { return farmerEmail; }
    public void setFarmerEmail(String farmerEmail) { this.farmerEmail = farmerEmail; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }

    public String getRationCardNumber() { return rationCardNumber; }
    public void setRationCardNumber(String rationCardNumber) { this.rationCardNumber = rationCardNumber; }

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public String getPassbookFile() { return passbookFile; }
    public void setPassbookFile(String passbookFile) { this.passbookFile = passbookFile; }

    public String getLandProofFile() { return landProofFile; }
    public void setLandProofFile(String landProofFile) { this.landProofFile = landProofFile; }
}
