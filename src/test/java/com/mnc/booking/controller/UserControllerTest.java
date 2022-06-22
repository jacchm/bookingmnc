package com.mnc.booking.controller;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.ClientProtocolException;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpGet;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpResponse;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;
import com.mnc.booking.model.User;
import io.zonky.test.db.util.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

  @LocalServerPort
  int port;

  private String host;

  private RestTemplate restTemplate;


  @Autowired
  Environment environment;

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  private TestRestTemplate testRestTemplate; // available with Spring Web MVC


  @BeforeEach
  public void beforeTest() throws UnknownHostException {
    host = InetAddress.getLocalHost().getHostAddress();
    restTemplate = new RestTemplate();
    // restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
  }

  @Test
  void httpClientExample() throws UnknownHostException {

    ParameterizedTypeReference<User> user = new ParameterizedTypeReference<>() {
    };
    ResponseEntity<User> response = testRestTemplate.exchange("http://" + host + ":" + port + "/users/" + "admin", HttpMethod.GET, null, user);

    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(Objects.requireNonNull(response.getBody()).getUsername()).isEqualTo("admin");
    assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Admin");
    assertThat(Objects.requireNonNull(response.getBody()).getVersion()).isEqualTo(0);
  }

  @Test
  void createUser() throws Exception {

    // Given
    String username = "admin";
    HttpUriRequest request = new HttpGet("http://localhost:" + port + "/users" + username);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

    // Then
    assertThat(httpResponse.getCode()).isEqualTo(HttpStatus.SC_OK);
  }

  @Test
  public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
      throws ClientProtocolException, IOException {

    // Given
    String username = RandomStringUtils.randomAlphabetic(8);
    HttpUriRequest request = new HttpGet("http://api.github.com/users/" + username);

    // When
    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

    // Then
    assertThat(httpResponse.getCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  void getUsers() {
  }

  @Test
  void getUser() {
  }

  @Test
  void updateUserRoles() {
  }

  @Test
  void deleteUser() {
  }
}
