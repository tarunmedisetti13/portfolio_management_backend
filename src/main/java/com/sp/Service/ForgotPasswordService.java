package com.sp.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sp.Repository.ForgotPasswordRepository;
import com.sp.Repository.UserRepository;
import com.sp.dto.MailBody;
import com.sp.entities.ForgotPassword;
import com.sp.entities.User;
import com.sp.utils.ChangePassword;

@Service
public class ForgotPasswordService {

    @Autowired
    ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<String> verifyEmail(String email) {
        // Check if the email exists in the system
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            return new ResponseEntity<>("User email not found", HttpStatus.NOT_FOUND);
        }

        // Generate OTP
        int otp = otpGenerator();

        // Send OTP to the user's email
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request: " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        emailService.sendSimpleMessage(mailBody);

        // Create ForgotPassword entry with expiration time set to 10 minutes (600 seconds)
        ZonedDateTime expirationTime = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(10);  // Expiry time in 10 minutes
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .email(email)
                .expirationTime(expirationTime)  // Store ZonedDateTime
                .build();

        System.out.println(forgotPassword);
        // Save ForgotPassword entry
        forgotPasswordRepository.save(forgotPassword);

        return ResponseEntity.ok("Enter the OTP sent to your email");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(10000, 999999);  // Generate OTP in range 10000-999999
    }

    public ResponseEntity<String> verifyOtp(Integer otp, String email) {
        // Fetch the ForgotPassword entry based on OTP and email
        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndEmail(otp, email).orElse(null);

        if (forgotPassword == null) {
            // Invalid OTP for email
            return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
        }

        // Convert ZonedDateTime to Instant for comparison
        ZonedDateTime expirationTime = forgotPassword.getExpirationTime();
        Instant expirationInstant = expirationTime.toInstant();  // Convert to Instant

        // Debugging prints for expirationTime and current time
        System.out.println("Expiration Time: " + expirationInstant);
        System.out.println("Current Time: " + Instant.now());

        // Check if the OTP has expired
        if (expirationInstant.isBefore(Instant.now())) {
            // OTP has expired
            forgotPasswordRepository.delete(forgotPassword);
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        // OTP verified successfully
        return ResponseEntity.ok("OTP Verified");
    }

    public ResponseEntity<String> changePasswordHandler(String password, String email) {
        password = URLDecoder.decode(password, StandardCharsets.UTF_8);  // Decode the password if encoded

        if (password == null || password.isEmpty()) {
            return new ResponseEntity<>("Password cannot be empty", HttpStatus.BAD_REQUEST);
        }
System.out.println("p="+password);
        userRepository.updatePassword(email, password);
        return ResponseEntity.ok("Password has been changed");
    }

}
