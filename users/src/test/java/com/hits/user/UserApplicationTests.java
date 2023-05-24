package com.hits.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.*;
import com.hits.common.dto.user.FullNameDto;
import com.hits.common.dto.user.PaginationQueryDto;
import com.hits.user.dto.*;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.hits.common.Paths.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @LocalServerPort
    private Integer port;
    @Rule
    public WireMockRule serviceMock = new WireMockRule(new WireMockConfiguration().notifier(new Slf4jNotifier(true)));
    private WireMockServer server;

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

    @BeforeEach
    void beforeEach(){
        server = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8082));
        server.start();
    }

    @AfterEach
    void afterEach() {
        server.stop();
    }

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
    @Order(1)
    void contextLoads() {
        log.info("context load");
    }

    @SneakyThrows
    @Test
    @Order(2)
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
    @Order(3)
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

    }

    @SneakyThrows
    @Test
    @Order(4)
    void getMeTest() {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("testlogin1");
        credentialsDto.setPassword("1234qwErt@");

        MvcResult result = mockMvc.perform(post(USERS_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk()).andReturn();

        String header = result.getResponse().getHeader("Authorization");

        result = mockMvc.perform(get(USERS_GET_ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

        UserDto userDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);

        assertEquals(userDto.getLogin(), "testlogin1");
        assertEquals(userDto.getBirthDate(), new Date(2123123));
        assertEquals(userDto.getFullName().getName(), "test1");
        assertEquals(userDto.getFullName().getPatronymic(), "test2");
        assertEquals(userDto.getFullName().getSurname(), "test3");
    }


    @SneakyThrows
    @Test
    @Order(5)
    void putMeTest() {

        server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.patch(urlEqualTo("/friends/common/synchronise"))
                .willReturn(ok()));

        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("testlogin1");
        credentialsDto.setPassword("1234qwErt@");

        MvcResult result = mockMvc.perform(post(USERS_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk()).andReturn();

        String header = result.getResponse().getHeader("Authorization");

        UserEditDto userEditDto  = new UserEditDto();
        FullNameDto fullNameDto = new FullNameDto();
        fullNameDto.setName("test11");
        fullNameDto.setPatronymic("test21");
        fullNameDto.setSurname("test31");
        userEditDto.setAvatarId(UUID.randomUUID());
        userEditDto.setBirthDate(new Date(21231231));
        userEditDto.setCity("Tomsk1");
        userEditDto.setEmail("email@email.com");
        userEditDto.setFullName(fullNameDto);
        userEditDto.setLogin("testlogin11");
        userEditDto.setPhone("892340912341");



        result = mockMvc.perform(put(USERS_PUT_ME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(userEditDto))
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

        UserDto userDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(userDto.getLogin(), "testlogin11");
        assertEquals(userDto.getBirthDate(), new Date(21231231));
        assertEquals(userDto.getFullName().getName(), "test11");
        assertEquals(userDto.getFullName().getPatronymic(), "test21");
        assertEquals(userDto.getFullName().getSurname(), "test31");
    }

    @SneakyThrows
    @Test
    @Order(6)
    void getUser() {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("testlogin11");
        credentialsDto.setPassword("1234qwErt@");

        MvcResult result = mockMvc.perform(post(USERS_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk()).andReturn();

        String header = result.getResponse().getHeader("Authorization");

        server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.post("/friends/common/blocking")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")));

        result = mockMvc.perform(get(USERS_GET_USER.replace("{login}","testlogin2"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

        UserDto userDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(userDto.getLogin(), "testlogin2");
        assertEquals(userDto.getBirthDate(), new Date(2123123));
        assertEquals(userDto.getFullName().getName(), "tester1");
        assertEquals(userDto.getFullName().getPatronymic(), "tester2");
        assertEquals(userDto.getFullName().getSurname(), "tester3");
    }

    @SneakyThrows
    @Test
    @Order(7)
    void getUsers() {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("testlogin11");
        credentialsDto.setPassword("1234qwErt@");

        MvcResult result = mockMvc.perform(post(USERS_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(credentialsDto)))
                .andExpect(status().isOk()).andReturn();

        String header = result.getResponse().getHeader("Authorization");

        UsersQueryDto usersQueryDto = new UsersQueryDto();
        PaginationQueryDto paginationQueryDto = new PaginationQueryDto();
        UserFiltersDto userFiltersDto = new UserFiltersDto();
        UserSortFieldDto userSortFieldDto = new UserSortFieldDto();

        usersQueryDto.setUserFiltersDto(userFiltersDto);
        usersQueryDto.setUserSortFieldDto(userSortFieldDto);
        usersQueryDto.setPaginationQueryDto(paginationQueryDto);

        result = mockMvc.perform(post(USERS_GET_USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersQueryDto))
                        .characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

    }
}
