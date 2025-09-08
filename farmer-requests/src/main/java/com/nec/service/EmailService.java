package com.nec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Loan notification
    public void sendLoanStatusEmail(String email, Double amount, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Loan Application Status Update");
        message.setText(
            "Dear Farmer,\n\n" +
            "Your loan request of amount: ₹" + amount + " has been " + status + ".\n\n" +
            (status.equalsIgnoreCase("APPROVED")
                ? "The loan amount will be processed to your account shortly."
                : "Unfortunately, your loan request has been rejected.") +
            "\n\nThank you for using our service.\n\n" +
            "Best regards,\n" +
            "Farm Assistance Team"
        );

        mailSender.send(message);
    }

    // Subsidy notification
    public void sendSubsidyStatusEmail(String email, Double amount, String status, String purpose) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Subsidy Application Status Update");
        message.setText(
            "Dear Farmer,\n\n" +
            "Your subsidy request for **" + purpose + "** with amount: ₹" + amount + " has been " + status + ".\n\n" +
            (status.equalsIgnoreCase("APPROVED")
                ? "The subsidy amount will be credited to your registered bank account."
                : "Unfortunately, your subsidy request has been rejected.") +
            "\n\nStay connected for future schemes.\n\n" +
            "Best regards,\n" +
            "Farm Assistance Team"
        );

        mailSender.send(message);
    }
}
