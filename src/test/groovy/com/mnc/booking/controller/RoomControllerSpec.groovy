package com.mnc.booking.controller

import com.mnc.booking.controller.dto.ErrorResponseDTO
import com.mnc.booking.controller.dto.room.RoomCreateResponseDTO
import com.mnc.booking.controller.dto.room.RoomDTO
import com.mnc.booking.controller.dto.room.URICreateResponseDTO
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
class RoomControllerSpec extends Specification implements RoomControllerTestData {

    @Shared
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine")

    @Autowired
    private TestRestTemplate testRestTemplate

    def "should create a new room and return status 201"() {
        given: 'new room to create with a new roomNo'
        def roomNo = "70"
        def newRoom = prepareRoomDTO(roomNo)

        when: 'trying to create a new room'
        def responseEntity = testRestTemplate.exchange("/rooms", HttpMethod.POST, new HttpEntity(newRoom), new ParameterizedTypeReference<RoomCreateResponseDTO>() {
        }, Map.of())

        then: 'response should return status 201 CREATED'
        responseEntity.statusCode == HttpStatus.CREATED
        responseEntity.getBody().getRoomNo() != null
        responseEntity.getBody().getRoomNo() == roomNo
    }

    def "should not create a new room and return CONFLICT status when the room with a given roomNo already exists"() {
        given: 'new room to create with existing roomNo'
        def existingRoomNo = "1"
        def newRoom = prepareRoomDTO(existingRoomNo)

        when: 'trying to create a new room'
        def responseEntity = testRestTemplate.exchange("/rooms", HttpMethod.POST, new HttpEntity(newRoom), new ParameterizedTypeReference<ErrorResponseDTO>() {
        }, Map.of())

        then: 'response should return status 409 CONFLICT'
        responseEntity.statusCode == HttpStatus.CONFLICT
        def errorResponseDTO = responseEntity.getBody()
        errorResponseDTO.getCode() == 409
        errorResponseDTO.getStatus() != null
        errorResponseDTO.getMessage() != null
    }

    //TODO: add test for GET rooms

    def "should retrieve a room and return status 200"() {
        given: 'roomNo'
        def roomNo = "5"

        when: 'trying to create a new room'
        def responseEntity = testRestTemplate.exchange("/rooms/{id}", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<RoomDTO>() {
        }, Map.of("id", roomNo))

        then: 'response should return status 200 OK'
        responseEntity.statusCode == HttpStatus.OK
        def roomResponse = responseEntity.getBody()
        roomResponse.getRoomNo() == roomNo
        roomResponse.getNoPeople() != 0
        roomResponse.getDescription() != null
        roomResponse.getRoomType() != null
        roomResponse.getPricePerNight() != null
        roomResponse.getIsBalcony() != null
        roomResponse.getIsOutstandingView() != null
        roomResponse.getIsTv() != null
        roomResponse.getBathroomType() != null
        roomResponse.getIsCoffeeMachine() != null
        roomResponse.getIsRestArea() != null
        roomResponse.getRoomSize() != null
        roomResponse.getImages() != null
        roomResponse.getStatus() != null
        roomResponse.getCreatedAt() != null
        roomResponse.getModifiedAt() != null
    }

    def "should not retrieve a room when does not exist and return status 404 NOT FOUND"() {
        given: 'roomNo'
        def roomNo = "non-existing"

        when: 'trying to create a new room'
        def responseEntity = testRestTemplate.exchange("/rooms/{id}", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<ErrorResponseDTO>() {
        }, Map.of("id", roomNo))

        then: 'response should return status 404 NOT FOUND'
        responseEntity.statusCode == HttpStatus.NOT_FOUND
        def roomResponse = responseEntity.getBody()
        roomResponse.getCode() == 404
        roomResponse.getStatus() != null
        roomResponse.getMessage() != null
    }

    def "should update a room and return status 204"() {
        given: 'room update'
        def roomNo = "10"
        def roomUpdate = prepareRoomDTO(roomNo)

        when: 'trying to create a new room'
        def responseEntity = testRestTemplate.exchange("/rooms/{id}", HttpMethod.PUT, new HttpEntity(roomUpdate), new ParameterizedTypeReference<Void>() {
        }, Map.of("id", roomNo))

        then: 'response should return status 204 NO_CONTENT'
        responseEntity.statusCode == HttpStatus.NO_CONTENT
    }

    def "should not update a room which does not exist and return status 404 NOT FOUND"() {
        given: 'room update'
        def roomNo = "non-existing"
        def roomUpdate = prepareRoomDTO(roomNo)

        when: 'trying to create a new room'
        def responseEntity = testRestTemplate.exchange("/rooms/{id}", HttpMethod.PUT, new HttpEntity(roomUpdate), new ParameterizedTypeReference<ErrorResponseDTO>() {
        }, Map.of("id", roomNo))

        then: 'response should return status 404 NOT FOUND'
        responseEntity.statusCode == HttpStatus.NOT_FOUND
        def roomResponse = responseEntity.getBody()
        roomResponse.getCode() == 404
        roomResponse.getStatus() != null
        roomResponse.getMessage() != null
    }

    def "should delete a room and return status 204"() {
        given: 'room'
        def roomNo = "20"

        when: 'trying to create a new room'
        def responseEntity = testRestTemplate.exchange("/rooms/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, new ParameterizedTypeReference<Void>() {
        }, Map.of("id", roomNo))

        then: 'response should return status 204 NO_CONTENT'
        responseEntity.statusCode == HttpStatus.NO_CONTENT
    }

    def "should add a new image uri to an existing room and return status 201 CREATED"() {
        given: 'roomNo and new image URI'
        def roomNo = "19"
        def newURI = prepareURIDTO(roomNo)

        when: 'trying to create a new uri'
        def responseEntity = testRestTemplate.exchange("/rooms/{id}/uris", HttpMethod.POST, new HttpEntity(newURI), new ParameterizedTypeReference<URICreateResponseDTO>() {
        }, Map.of("id", roomNo))

        then: 'response should return status 201 CREATED'
        responseEntity.statusCode == HttpStatus.CREATED
        responseEntity.getBody().getUriId() != null
    }

    def "should delete an uri and return status 204"() {
        given: 'roomNo and new image URI'
        def roomNo = "19"
        def uriId = "38"
        def newURI = prepareURIDTO(roomNo)

        when: 'trying to create a new uri'
        def responseEntity = testRestTemplate.exchange("/rooms/{id}/uris/{uriId}", HttpMethod.DELETE, new HttpEntity(newURI), new ParameterizedTypeReference<Void>() {
        }, Map.of("id", roomNo, "uriId", uriId))

        then: 'response should return status 204 NO CONTENT'
        responseEntity.statusCode == HttpStatus.NO_CONTENT
    }


}
