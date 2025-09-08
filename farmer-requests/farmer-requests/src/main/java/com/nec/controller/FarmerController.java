package com.nec.controller;

import com.nec.entity.Farmer;
import com.nec.entity.Loan;
import com.nec.entity.Subsidy;
import com.nec.repository.FarmerRepo;
import com.nec.repository.LoanRepo;
import com.nec.repository.SubsidyRepo;
import com.nec.service.EmailService;
import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins = "https://lovely-profiterole-5b8424.netlify.app")
@RestController
public class FarmerController {
	
	

    @Autowired
    private FarmerRepo farmerRepo;

    @Autowired
    private LoanRepo loanRepo;
    
    @Autowired
    private SubsidyRepo subsidyRepo;
    
    @Autowired
    private EmailService emailService;
    
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    // --- Signup ---
    @PostMapping("/signup")
    public Farmer signup(@RequestBody Farmer farmer) {
        return farmerRepo.save(farmer);
    }

    
    
    
    // --- Login ---
    @PostMapping("/farmerLogin")
    public Map<String, Boolean> farmerLogin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Farmer farmer = farmerRepo.findById(email).orElse(null);

        Map<String, Boolean> response = new HashMap<>();
        if (farmer != null && farmer.getPassword().equals(password)) {
            response.put("valid", true);
        } else {
            response.put("valid", false);
        }
        return response;
    }

    
    
    @PostMapping("/applyLoan")
    public Map<String, String> applyLoan(
            @RequestParam String email,
            @RequestParam Double loanAmount,
            @RequestParam String purpose,
            @RequestParam String aadharNumber,
            @RequestParam String rationCardNumber,
            @RequestParam String bankAccountNumber,
            @RequestParam("passbookFile") MultipartFile passbookFile,
            @RequestParam("landProofFile") MultipartFile landProofFile
    ) {
        Loan loan = new Loan();
        loan.setFarmerEmail(email);
        loan.setAmount(loanAmount);
        loan.setPurpose(purpose);
        loan.setAadharNumber(aadharNumber);
        loan.setRationCardNumber(rationCardNumber);
        loan.setBankAccountNumber(bankAccountNumber);

        loan.setPassbookFile(passbookFile.getOriginalFilename());
        loan.setLandProofFile(landProofFile.getOriginalFilename());
        loan.setStatus("PENDING");

        loanRepo.save(loan);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Loan applied successfully");
        response.put("status", "PENDING");
        return response;
    }


    
    
    
    

 // Get all loans for a specific farmer
    @GetMapping("/myLoans")
    public List<Loan> getMyLoans(@RequestParam String email) {
        // Assuming your Loan entity now has a field `farmerEmail` as String
        return loanRepo.findByFarmerEmail(email);
    }




    
 
    @GetMapping("/pendingLoans")
    public List<Loan> getPendingLoans() {
        return loanRepo.findByStatus("PENDING");
    }
    
    
    
    
    
    
    @PostMapping("/updateLoanStatus")
    public Map<String, String> updateLoanStatus(@RequestBody Map<String, Object> request) {
        Long loanId = Long.valueOf(request.get("loanId").toString());
        String status = (String) request.get("status"); // APPROVED / REJECTED

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus(status);
        loanRepo.save(loan);

      
        emailService.sendLoanStatusEmail(loan.getFarmerEmail(), loan.getAmount(), status);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Loan status updated & email sent");
        response.put("status", status);
        return response;
    }
    
    
    
    
    @PostMapping("/applySubsidy")
    public Map<String, String> applySubsidy(
            @RequestParam String email,
            @RequestParam Double amount,
            @RequestParam String purpose,
            @RequestParam String aadharNumber,
            @RequestParam String rationCardNumber,
            @RequestParam String bankAccountNumber,
            @RequestParam("passbookFile") MultipartFile passbookFile,
            @RequestParam("landProofFile") MultipartFile landProofFile
    ) {
        Subsidy subsidy = new Subsidy();
        subsidy.setFarmerEmail(email);
        subsidy.setAmount(amount);
        subsidy.setPurpose(purpose);
        subsidy.setAadharNumber(aadharNumber);
        subsidy.setRationCardNumber(rationCardNumber);
        subsidy.setBankAccountNumber(bankAccountNumber);

        // Save file names (for now, later we can store actual files in /uploads folder)
        subsidy.setPassbookFile(passbookFile.getOriginalFilename());
        subsidy.setLandProofFile(landProofFile.getOriginalFilename());

        subsidy.setStatus("PENDING");

        subsidyRepo.save(subsidy);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Subsidy applied successfully");
        response.put("status", "PENDING");
        return response;
    }



    
    @GetMapping("/pendingSubsidies")
    public List<Subsidy> getPendingSubsidies() {
        return subsidyRepo.findByStatus("PENDING");
    }

    
    
    @PostMapping("/updateSubsidyStatus")
    public Map<String, String> updateSubsidyStatus(@RequestBody Map<String, Object> request) {
        Long id = Long.valueOf(request.get("id").toString());
        String status = (String) request.get("status");

        Subsidy subsidy = subsidyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subsidy not found"));

        subsidy.setStatus(status);
        subsidyRepo.save(subsidy);

        // ✅ Send email notification
        emailService.sendSubsidyStatusEmail(subsidy.getFarmerEmail(), subsidy.getAmount(), status, "Agriculture Support");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Subsidy status updated & email sent");
        response.put("status", status);
        return response;
    }

    
    @GetMapping("/mySubsidies")
    public List<Subsidy> getMySubsidies(@RequestParam String email) {
        return subsidyRepo.findByFarmerEmail(email);
    }
    
    
    
    
    
    
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> payload) {
        Map<String, String> responseData = new HashMap<>();
        try {
            String email = (String) payload.get("email");
            Long amount = Long.valueOf(payload.get("amount").toString());

            if (email == null || email.isBlank()) {
                responseData.put("error", "Email is required");
                return ResponseEntity.badRequest().body(responseData);
            }

            Stripe.apiKey = stripeApiKey;

            // ✅ Update loan
            Loan loan = loanRepo.findFirstByFarmerEmailOrderByIdAsc(email)
                    .orElseThrow(() -> new RuntimeException("Loan not found for email: " + email));

            double newAmount = loan.getAmount() - amount;
            loan.setAmount(Math.max(newAmount, 0));
            loanRepo.save(loan);

            // ✅ Create Stripe checkout session
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://lovely-profiterole-5b8424.netlify.app/farmer_dashboard?session_id={CHECKOUT_SESSION_ID}&email=" + email)
                    .setCancelUrl("https://lovely-profiterole-5b8424.netlify.app/farmer_dashboard/cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amount * 100)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Loan Repayment")
                                                                    .build())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            responseData.put("id", session.getId());
            responseData.put("message", "Loan amount updated");
            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    
    

  
}
