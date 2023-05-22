package com.hits.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hits.common.dto.user.FullNameDto;
import com.hits.user.dto.CredentialsDto;
import com.hits.user.dto.UserRegisterDto;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import static com.hits.common.Paths.USERS_SIGN_IN;
import static com.hits.common.Paths.USERS_SIGN_UP;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
//@TestPropertySource("/test.properties")
class UserApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private TestRestTemplate testRestTemplate;

    static RabbitMQContainer rabbitMQContainer ;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String header = "";

    @LocalServerPort
    private Integer port;

    static {
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.10.6-management-alpine")
                .withExposedPorts(5672, 15672)
                .withPluginsEnabled("rabbitmq_management")
                .withUser("user", "password", ImmutableSet.of("administrator"))
                .withVhost("work_host")
                .withPermission("work_host", "user", ".*", ".*", ".*")
                .withStartupTimeout(Duration.of(100, SECONDS));

    }

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        rabbitMQContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        rabbitMQContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
        registry.add("rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }
    @Test
    void contextLoads() {

    }

    @SneakyThrows
    @Test
    void registerUserTest() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        FullNameDto fullNameDto = new FullNameDto();
        fullNameDto.setName("test1");
        fullNameDto.setPatronymic("test2");
        fullNameDto.setSurname("test3");
        userRegisterDto.setAvatarId(UUID.randomUUID());
        userRegisterDto.setBirthDate(new Date(2123123));
        userRegisterDto.setCity("Tomsk");
        userRegisterDto.setEmail("email@email.com");
        userRegisterDto.setFullName(fullNameDto);
        userRegisterDto.setLogin("testlogin1");
        userRegisterDto.setPhone("89234091234");
        userRegisterDto.setPassword("1234qwErt@");

        mockMvc.perform(post(USERS_SIGN_UP)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(userRegisterDto)))
                .andExpect(status().isOk());

        fullNameDto.setName("tester1");
        fullNameDto.setPatronymic("tester2");
        fullNameDto.setSurname("tester3");
        userRegisterDto.setAvatarId(UUID.randomUUID());
        userRegisterDto.setBirthDate(new Date(2123123));
        userRegisterDto.setCity("Tomsk");
        userRegisterDto.setEmail("email1@email.com");
        userRegisterDto.setFullName(fullNameDto);
        userRegisterDto.setLogin("testlogin2");
        userRegisterDto.setPhone("89234091234");
        userRegisterDto.setPassword("1234qwErt@");

        MvcResult result = mockMvc.perform(post(USERS_SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userRegisterDto)))
                .andExpect(status().isOk()).andReturn();

        log.warn(result.getResponse().getContentAsString());


        userRegisterDto.setPassword("1234qwert@");

        mockMvc.perform(post(USERS_SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userRegisterDto)))
                .andExpect(status().isBadRequest());

        userRegisterDto.setPassword("1234qwErt@");

        mockMvc.perform(post(USERS_SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userRegisterDto)))
                .andExpect(status().isConflict());

    }

    @SneakyThrows
    @Test
    void singInTest() {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("testlogin1");
        credentialsDto.setPassword("1234qweRt@");

        mockMvc.perform(post(USERS_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isUnauthorized());

        credentialsDto.setPassword("1234qwErt@");

        MvcResult result = mockMvc.perform(post(USERS_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk()).andReturn();

        header = result.getResponse().getHeader("Authorization");
        log.info(header);
    }
}
