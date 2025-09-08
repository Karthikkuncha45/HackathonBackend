package com.nec.entity;

import jakarta.persistence.*;

@Entity
public class Subsidy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String farmerEmail;
    private Double amount;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    private String purpose;

    // New fields
    private String aadharNumber;
    private String rationCardNumber;
    private String bankAccountNumber;

    private String passbookFile;  // store uploaded file path or name
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
