package com.sp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.entities.ForgotPassword;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    Optional<ForgotPassword> findByOtpAndEmail(Integer otp, String email);
}
