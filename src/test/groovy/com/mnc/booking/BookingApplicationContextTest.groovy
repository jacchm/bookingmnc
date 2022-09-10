package com.mnc.booking

import com.mnc.booking.model.URI
import com.mnc.booking.repository.URIRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

    // below works only for static postgres container
//    @DynamicPropertySource
//    static void configureTestcontainersProperties (DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl())
//        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername())
//        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword())
//    }

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
