package com.example.cardtocard.repo;

import com.example.cardtocard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserById(Long id);
    UserDetails getUserByUsername(String username);

    boolean existsByUsername(String username);
}
