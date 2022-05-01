package com.mnc.booking.repository;

import com.mnc.booking.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

  Optional<User> findByUsername(final String username);

  @Modifying
  @Query("update User u set u.authorities = ?2 where u.username = ?1")
  Optional<Integer> setUserAuthoritiesByUsername(final String username, final String authorities);

  @Modifying
  @Query("update User u set u.password = ?3 where u.username = ?1 and u.password = ?2")
  Optional<Integer> updateUserPasswordByUsername(final String username, final String oldPassword, final String newPassword);

  @Override
  @Modifying
  @Query(nativeQuery = true, value = "DELETE FROM users WHERE username = ?1")
  void deleteById(final String username);

}
