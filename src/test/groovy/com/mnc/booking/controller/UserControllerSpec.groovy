package com.mnc.booking.controller

import com.mnc.booking.controller.dto.ErrorResponseDTO
import com.mnc.booking.controller.dto.user.UserCreateResponseDTO
import com.mnc.booking.controller.dto.user.UserDTO
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
class UserControllerSpec extends Specification implements UserControllerTestData {

    @Shared
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine")

    @Autowired
    private TestRestTemplate testRestTemplate

    def "should create a new user and return status 201"() {
        given: 'new user to create with a new userNo'
        def email = "testuseremail@gmail.com"
        def username = "testuser"
        def newUser = prepareUserDTO(email, username)

        when: 'trying to create a new user'
        def responseEntity = testRestTemplate.exchange("/users", HttpMethod.POST, new HttpEntity(newUser), new ParameterizedTypeReference<UserCreateResponseDTO>() {
        }, Map.of())

        then: 'response should return status 201 CREATED'
        responseEntity.statusCode == HttpStatus.CREATED
        responseEntity.getBody().getUsername() == username
    }

    def "should not create a new user and return CONFLICT status when the user with a given username already exists"() {
        given: 'new user to create with existing userNo'
        def existingUsername = "receptionist"
        def email = "testuseremail@gmail.com"
        def newUser = prepareUserDTO(email, existingUsername)

        when: 'trying to create a new user'
        def responseEntity = testRestTemplate.exchange("/users", HttpMethod.POST, new HttpEntity(newUser), new ParameterizedTypeReference<ErrorResponseDTO>() {
        }, Map.of())

        then: 'response should return status 409 CONFLICT'
        responseEntity.statusCode == HttpStatus.CONFLICT
        def errorResponseDTO = responseEntity.getBody()
        errorResponseDTO.getCode() == 409
        errorResponseDTO.getStatus() == HttpStatus.CONFLICT.getReasonPhrase()
        errorResponseDTO.getMessage() != null
    }

    def "should retrieve a user and return status 200"() {
        given: 'username'
        def username = "receptionist"

        when: 'trying to retrieve a user'
        def responseEntity = testRestTemplate.exchange("/users/{username}", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<UserDTO>() {
        }, Map.of("username", username))

        then: 'response should return status 200 OK'
        responseEntity.statusCode == HttpStatus.OK
        def userResponse = responseEntity.getBody()
        userResponse.getUsername() == username
        userResponse.getEmail() != null
        userResponse.getName() != null
        userResponse.getSurname() != null
        userResponse.getDateOfBirth() != null
        userResponse.getAuthorities() != null
        userResponse.getPhoneNumber() != null
    }

    def "should not retrieve a user when does not exist and return status 404 NOT FOUND"() {
        given: 'userNo'
        def username = "non-existing"

        when: 'trying to create a new user'
        def responseEntity = testRestTemplate.exchange("/users/{username}", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<ErrorResponseDTO>() {
        }, Map.of("username", username))

        then: 'response should return status 404 NOT FOUND'
        responseEntity.statusCode == HttpStatus.NOT_FOUND
        def userResponse = responseEntity.getBody()
        userResponse.getCode() == 404
        userResponse.getStatus() != null
        userResponse.getMessage() != null
    }

//    def "should update a user role and return status 204"() {
//        given: 'username'
//        def username = "user"
//        def userRolesUpdate = prepareUserRolesUpdateDTO("ROLE_RECEPTIONIST")
//
//        when: 'trying to create a new user'
//        def responseEntity = testRestTemplate.exchange("/users/{username}", HttpMethod.PATCH, new HttpEntity(userRolesUpdate), new ParameterizedTypeReference<Void>() {
//        }, Map.of("username", username))
//
//        then: 'response should return status 204 NO_CONTENT'
//        responseEntity.statusCode == HttpStatus.NO_CONTENT
//    }
//
//    def "should not update a user role and return status 404 NOT FOUND"() {
//        given: 'user update'
//        def username = "non-existing"
//        def userRolesUpdate = prepareUserRolesUpdateDTO(username)
//
//        when: 'trying to create a new user'
//        def responseEntity = testRestTemplate.exchange("/users/{username}", HttpMethod.PATCH, new HttpEntity(userRolesUpdate), new ParameterizedTypeReference<ErrorResponseDTO>() {
//        }, Map.of("username", username))
//
//        then: 'response should return status 404 NOT FOUND'
//        responseEntity.statusCode == HttpStatus.NOT_FOUND
//        def userResponse = responseEntity.getBody()
//        userResponse.getCode() == 404
//        userResponse.getStatus() == HttpStatus.NOT_FOUND.getReasonPhrase()
//        userResponse.getMessage() != null
//    }

    def "should delete a user and return status 204"() {
        given: 'user'
        def userNo = "20"

        when: 'trying to create a new user'
        def responseEntity = testRestTemplate.exchange("/users/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, new ParameterizedTypeReference<Void>() {
        }, Map.of("id", userNo))

        then: 'response should return status 204 NO_CONTENT'
        responseEntity.statusCode == HttpStatus.NO_CONTENT
    }

}
