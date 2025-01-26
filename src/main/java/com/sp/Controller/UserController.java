package com.sp.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.Service.UserService;
import com.sp.entities.LoginRequest;
import com.sp.entities.Portfolio;
import com.sp.entities.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody User newUser) {
       return userService.signupUser(newUser);
          
    }

    
    // Fetch portfolios for a user by username
    @GetMapping("/{username}/portfolios")
    public ResponseEntity<?> getUserPortfolios(@PathVariable String username) {
        try {
            // Get the user by username
            User user = userService.getUserByUsername(username);

            // Check if the user is null
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Invalid username"
                ));
            }

            // Fetch portfolios for the user
            List<Portfolio> portfolios = user.getPortfolios(); // Assuming a `getPortfolios()` method exists in the User entity

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Portfolios fetched successfully",
                "portfolios", portfolios
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Server error: Unable to fetch portfolios"
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Fetch the user by username
            User user = userService.getUserByUsername(loginRequest.getUsername());

            if (user == null) {
                return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Invalid username"
                ));
            }

            // Verify credentials
            boolean isAuthenticated = userService.verifyCredentials(loginRequest.getUsername(), loginRequest.getPassword());
            if (!isAuthenticated) {
                return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Incorrect password"
                ));
            }

            // Return the username and the token
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful!",
                "token", "mock-token", // Replace with real token generation logic
                "username", user.getUsername() // Send the username in the response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Server error during login"
            ));
        }
    }

    @GetMapping("/{username}/id")
    public ResponseEntity<String> getUserUsernameByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
