package com.mnc.booking.repository;

import com.mnc.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

  Optional<User> findByUsername(final String username);

  @Modifying
  @Query("update User u set u.authorities = ?2 where u.username = ?1")
  Optional<Integer> setUserAuthoritiesByUsername(final String username, final String authorities);

  @Modifying
  @Query("update User u set u.password = ?3 where u.username = ?1 and u.password = ?2")
  Optional<Integer> updateUserPasswordByUsername(final String username, final String oldPassword, final String newPassword);

  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM appuser WHERE username = ?1")
  void deleteById(final String username);

}
