package com.mnc.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnc.booking.model.User;
import com.mnc.booking.repository.UserRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql({"test-data.sql"})
@AutoConfigureEmbeddedDatabase
//@AutoConfigureMockMvc
class WebControllerTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ApplicationContext applicationContext;


  @Test
  public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

//    // given - precondition or setup
//    User employee = User.builder()
//        .name("Ramesh")
//        .surname("Fadatare")
//        .email("ramesh@gmail.com")
//        .build();
//
//    // when - action or behaviour that we are going test
//    ResultActions response = mockMvc.perform(post("/users")
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(employee)));
//
//    // then - verify the result or output using assert statements
//    response.andDo(print()).
//        andExpect(status().isCreated())
//        .andExpect(jsonPath("$.name",
//            is(employee.getName())))
//        .andExpect(jsonPath("$.surname",
//            is(employee.getSurname())))
//        .andExpect(jsonPath("$.email",
//            is(employee.getEmail())));

  }

  @Test
  public void testEmbeddedDatabase() {
    Optional<User> userOptional = userRepository.findByUsername("admin");

    assertThat(userOptional).hasValueSatisfying(user -> {
      assertThat(user.getUsername()).isNotNull();
      assertThat(user.getName()).isEqualTo("Admin");
      assertThat(user.getSurname()).isEqualTo("Administrator");
    });
  }


}
