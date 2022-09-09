package com.mnc.booking.repository;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureEmbeddedDatabase
class UserRepositoryTest {

  @Test
  void findByUsername() {
  }

  @Test
  void setUserAuthoritiesByUsername() {
  }

  @Test
  void updateUserPasswordByUsername() {
  }

  @Test
  void deleteById() {
  }
}
