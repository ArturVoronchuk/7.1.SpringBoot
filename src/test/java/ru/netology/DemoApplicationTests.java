package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
    private static final String ENDPOINT = "/profile";

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {
        devApp.start();
        prodApp.start();
    }

    @Container
    private static final GenericContainer<?> devApp = new GenericContainer<>("devapp")
            .withExposedPorts(8080);

    @Container
    private static final GenericContainer<?> prodApp = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);


    @Test
    void devAppContainer() {
        final String expected = "Current profile is dev";
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + devApp.getMappedPort(8080) + ENDPOINT, String.class);
        System.out.println(forEntity.getBody());
        Assertions.assertEquals(expected, forEntity.getBody());
    }

    @Test
    void prodAppContainer() {
        final String expected = "Current profile is prod";
        ResponseEntity<String> forEntityTwo = restTemplate.getForEntity("http://localhost:" + prodApp.getMappedPort(8081) + ENDPOINT, String.class);
        System.out.println(forEntityTwo.getBody());
        Assertions.assertEquals(expected, forEntityTwo.getBody());
    }
}