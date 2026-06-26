package com.ishika.grievance.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ishika.grievance.entity.User;
import com.ishika.grievance.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    long countByRole(Role role);
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
