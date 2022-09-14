package com.mnc.booking.controller

import com.mnc.booking.controller.dto.auth.AuthResponseDTO
import com.mnc.booking.controller.dto.user.UserCreateResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = [
                "spring.datasource.url=jdbc:tc:postgresql:11-alpine://testcontainers/booking"
        ]
)
@Testcontainers
class LoginControllerSpec extends Specification implements UserControllerTestData {

    @Shared
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine")

    @Autowired
    private TestRestTemplate testRestTemplate

    def "should register a new user and return status 201"() {
        given: 'new user to create with a new userNo'
        def email = "testuseremail@gmail.com"
        def username = "testuser"
        def newUser = prepareUserDTO(email, username)

        when: 'trying to register a new user'
        def responseEntity = testRestTemplate.exchange("/register", HttpMethod.POST, new HttpEntity(newUser), new ParameterizedTypeReference<UserCreateResponseDTO>() {
        }, Map.of())

        then: 'response should return status 201 CREATED'
        responseEntity.statusCode == HttpStatus.CREATED
        responseEntity.getBody().getUsername() == username
    }

    def "should retrieve an access token when logged in successfully"() {
        given: 'new user to create with a new userNo'
        def username = "admin"
        def password = "admin"
        def loginUser = prepareAuthRequest(username, password)

        when: 'trying to log in'
        def responseEntity = testRestTemplate.exchange("/login", HttpMethod.POST, new HttpEntity(loginUser), new ParameterizedTypeReference<AuthResponseDTO>() {
        }, Map.of())

        then: 'response should return status 200 OK'
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.getBody().getAccessToken() != null
    }
}
