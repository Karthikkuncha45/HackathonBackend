package com.nec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nec.entity.Subsidy;
import java.util.List;

public interface SubsidyRepo extends JpaRepository<Subsidy, Long> {
    List<Subsidy> findByStatus(String status);
    List<Subsidy> findByFarmerEmail(String email);
}
