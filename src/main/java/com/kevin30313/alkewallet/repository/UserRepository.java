package com.kevin30313.alkewallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevin30313.alkewallet.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);

}
