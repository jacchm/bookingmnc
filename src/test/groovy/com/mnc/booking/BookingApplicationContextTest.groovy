package com.mnc.booking

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:tc:postgresql:11-alpine://testcontainers/booking")
@Testcontainers
class BookingApplicationContextTest extends Specification {

    @Shared
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("Simnes49is")

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
}
