package com.mnc.booking.repository;

import com.mnc.booking.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

  Optional<User> findByUsername(final String username);

  @Override
  @Transactional
  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM users WHERE username = ?1")
  void deleteById(final String username);
}
