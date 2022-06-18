package com.mnc.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnc.booking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WebControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

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


}
