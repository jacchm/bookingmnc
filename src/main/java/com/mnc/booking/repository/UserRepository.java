package com.mnc.booking.repository;

import com.mnc.booking.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
  Optional<User> findByUsername(final String username);
  Boolean existsByUsername(String username);
  Boolean existsByEmail(String email);
}
