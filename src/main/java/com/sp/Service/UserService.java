package com.sp.Service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sp.Repository.UserRepository;
import com.sp.entities.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    // Other CRUD methods...
    
    // Login verification logic
    public boolean verifyCredentials(String username, String password) {
        // Fetch user by username
        User user = userRepository.findByUsername(username);
        
        // Check if user exists and password matches
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        
        // Return false if no matching user or password
        return false;
    }

    // Get user by username
    public User getUserByUsername(String username) {
        // Fetch user by username
        User user = userRepository.findByUsername(username);
        
        // If user doesn't exist, throw a ResourceNotFoundException
        if (user == null) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        
        // Return the user if found
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

	public ResponseEntity<?> signupUser(User newUser) {
		// TODO Auto-generated method stub
		try {
	        // Check if the username already exists
	        User existingUser = userRepository.findByUsername(newUser.getUsername());
	        if (existingUser != null) {
	            return ResponseEntity.status(409).body(Map.of(
	                "success", false,
	                "message", "Username already exists"
	            ));
	        }

	        // Check if the email already exists
	        User existingEmail = userRepository.getUserByEmail(newUser.getEmail());
	        if (existingEmail != null) {
	            return ResponseEntity.status(409).body(Map.of(
	                "success", false,
	                "message", "Email already exists"
	            ));
	        }

	        // Save the new user to the database
	        newUser.setCreatedAt(LocalDateTime.now());
	        newUser.setUpdatedAt(LocalDateTime.now());
	        userRepository.save(newUser);

	        return ResponseEntity.status(201).body(Map.of(
	            "success", true,
	            "message", "Account created successfully!"
	        ));
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body(Map.of(
	            "success", false,
	            "message", "Server error during sign-up"
	        ));
	    }
	}

    
}
