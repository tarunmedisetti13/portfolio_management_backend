package com.sp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.Service.ForgotPasswordService;
import com.sp.entities.EmailTemp;
import com.sp.utils.ChangePassword;

@RestController
@RequestMapping("/passwordmanagement")
public class ForgotPasswordController {

    @Autowired
    ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        return forgotPasswordService.verifyEmail(email);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        return forgotPasswordService.verifyOtp(otp, email);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<String> changePasswordHandler(@RequestBody EmailTemp emailObj) {
        String password = emailObj.getPassword();
        String email = emailObj.getEmail();

        System.out.println("Received Email: " + email);  // Debugging line
        System.out.println("Received Password: " + password);  // Debugging line

        return forgotPasswordService.changePasswordHandler(password, email);
    }

    


}
