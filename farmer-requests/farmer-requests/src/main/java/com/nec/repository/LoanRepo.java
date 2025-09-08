package com.nec.repository;

import com.nec.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LoanRepo extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(String status);
    List<Loan> findByFarmerEmail(String farmerEmail);
	Loan findFirstByFarmerEmailAndStatus(String email, String string);
	Optional<Loan> findFirstByFarmerEmailOrderByIdAsc(String email);

}



