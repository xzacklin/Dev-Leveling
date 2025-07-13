package com.develeveling.backend.repository;

import com.develeveling.backend.entity.TargetCompany;
import com.develeveling.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TargetCompanyRepository extends JpaRepository<TargetCompany, Long> {
    Optional<TargetCompany> findByUserAndCompanyNameIgnoreCase(User user, String companyName);
}