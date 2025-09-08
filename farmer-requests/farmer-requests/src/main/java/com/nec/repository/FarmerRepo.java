package com.nec.repository;

import com.nec.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepo extends JpaRepository<Farmer, String> {
}
