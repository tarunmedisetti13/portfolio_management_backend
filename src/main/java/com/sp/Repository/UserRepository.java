package com.sp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sp.entities.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String> {  // Use String instead of Long
    User findByUsername(String username);
	User findByEmailIgnoreCase(String email);
	@Transactional
	@Modifying
	@Query("update User u set u.password = :password where u.email = :email")
	void updatePassword(@Param("email") String email, @Param("password") String password);
	User getUserByEmail(String email);
;
}
