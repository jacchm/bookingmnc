package com.mnc.booking

import com.mnc.booking.model.URI
import com.mnc.booking.repository.URIRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.env.Environment
import org.springframework.web.context.WebApplicationContext
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
class BookingApplicationContextTest extends Specification {

    @LocalServerPort
    int randomServerPort

    @Autowired
    private WebApplicationContext webApplicationContext

    @Autowired
    private Environment environment

    @Autowired
    URIRepository uriRepository

    @Shared
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("test")

    def setup() {
        uriRepository.deleteAll()
    }

    def "contextLoads"() {
        expect:
        1 == 1
    }

    def "should create uri in db"() {
        when:
        def uri = new URI(null, "100", "somelink")

        then:
        uriRepository.save(uri)
        def count = uriRepository.findAll().asCollection().stream().count()
        count > 0

    }
}
