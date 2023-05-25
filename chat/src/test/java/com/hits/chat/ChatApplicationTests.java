package com.hits.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.hits.chat.dto.*;
import com.hits.common.annotation.EnableApiKey;
import com.hits.common.annotation.EnableBaseExceptionHandler;
import com.hits.common.annotation.EnableJwt;
import com.hits.common.dto.user.FullNameDto;
import com.hits.common.dto.user.PaginationQueryDto;
import com.hits.common.service.Utils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.hits.common.Paths.*;
import static com.hits.common.Paths.CHAT_FIND_MESSAGES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChatApplicationTests {
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
    @SneakyThrows
    void beforeEach(){
        server = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8081));
        server.start();

        myId = UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648a");
        myName = new FullNameDto();
        myName.setName("my1");
        myName.setPatronymic("my2");
        myName.setSurname("my3");
        otherUsers = List.of(
                UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648b"),
                UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648c"),
                UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648d"),
                UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648e"),
                UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648f"),
                UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da6481")
        );
        otherUsersNames = new ArrayList<>();

        Integer counter = 0;
        for(UUID id : otherUsers) {
            FullNameDto dto = new FullNameDto();
            dto.setSurname(counter.toString());
            dto.setName(counter.toString());
            dto.setPatronymic(counter.toString());
            counter+=1;
            otherUsersNames.add(dto);
            server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/users/common/check/" + id.toString())
                    .willReturn(ok()
                            .withHeader("Content-Type", "application/json")
                            .withBody("true")));
            server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/users/common/user/" + id.toString())
                    .willReturn(ok()
                            .withHeader("Content-Type", "application/json")
                            .withBody(objectMapper.writeValueAsString(dto))));
        }
        server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/users/common/user/" + myId.toString())
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(myName))));
        server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/users/common/check/" + myId.toString())
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")));
        chat = new ArrayList<>();
        chatNames = new ArrayList<>();
        chatNames.add("first");
        chatNames.add("second");
        chatNames.add("third");

        chat.add(Set.of(otherUsers.get(0), otherUsers.get(1), otherUsers.get(2)));
        chat.add(Set.of(otherUsers.get(3)));
        chat.add(Set.of(otherUsers.get(0), otherUsers.get(1), otherUsers.get(2), otherUsers.get(3), otherUsers.get(4), otherUsers.get(5)));


        messagesUsers = new ArrayList<>();
        messagesUsers.add(List.of("Привет", "Не отвечаешь?", "Ладненько..."));
        messagesUsers.add(List.of("Добрый день"));
        messagesUsers.add(List.of("Ну что?", "Не отвечаешь?", "Ладненько..."));
        messagesUsers.add(List.of("Как там с деньгами?", "Не отвечаешь?", "Понятненько..."));
        messagesUsers.add(List.of("Тестовый текст", "Ладненько..."));
        messagesUsers.add(List.of("1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3"));

        messagesChat = new ArrayList<>();
        messagesChat.add(List.of("Чатик", "Вечер?", "Ладненько..."));
        messagesChat.add(List.of("Добрый вечер"));
        messagesChat.add(List.of("Ну?", "Отвечаешь?", "Ладнено."));
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

    private UUID myId;
    private FullNameDto myName;
    private List<UUID> otherUsers;
    private List<FullNameDto> otherUsersNames;
    private String header;
    private List<String> chatNames;
    private List<Set<UUID>> chat;
    private List<List<String>> messagesUsers;
    private List<List<String>> messagesChat;
    private List<UUID> chatIds = new ArrayList<>();
    @Test
    @Order(2)
    @SneakyThrows
    void sendPrivateMessageTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        for(int i = 0; i < 6; i++) {
            UUID id = otherUsers.get(i);
            for(String text : messagesUsers.get(i)){
                SendMessageDto sendMessageDto = new SendMessageDto();
                sendMessageDto.setText(text);
                sendMessageDto.setChatId(id);
                MvcResult result = mockMvc.perform(post(CHAT_SEND_PRIVATE_MESSAGE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(sendMessageDto))
                                .header("Authorization", header))
                        .andExpect(status().isOk()).andReturn();
            }
        }
    }

    @Test
    @Order(3)
    @SneakyThrows
    void createChatTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        for (int i = 0; i < 3; i++) {
            CreateChatDto createChatDto = new CreateChatDto();
            createChatDto.setName(chatNames.get(i));
            createChatDto.setUsersId(chat.get(i));
            MvcResult result = mockMvc.perform(post(CHAT_CREATE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(objectMapper.writeValueAsString(createChatDto))
                            .header("Authorization", header))
                    .andExpect(status().isOk()).andReturn();
        }
    }

    @Test
    @Order(4)
    @SneakyThrows
    void editChatTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        ChatQueryDto chatQueryDto = new ChatQueryDto();
        chatQueryDto.setPaginationQueryDto(new PaginationQueryDto());
        chatQueryDto.getPaginationQueryDto().setPageNumber(1);
        chatQueryDto.getPaginationQueryDto().setSize(1);
        chatQueryDto.setNameFilter("first");
        MvcResult result = mockMvc.perform(post(CHAT_FIND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(chatQueryDto))
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

        ChatFindInfoPagDto chatFindInfoPagDto = objectMapper.readValue(result.getResponse().getContentAsString(), ChatFindInfoPagDto.class);

        EditChatDto editChatDto = new EditChatDto();
        editChatDto.setChatId(chatFindInfoPagDto.getChatFindInfoDtos().get(0).getId());
        editChatDto.setName(chatNames.get(0) + "1");
        editChatDto.setUsersId(chat.get(0));
        editChatDto.setAvatarId(otherUsers.get(0));
        result = mockMvc.perform(put(CHAT_EDIT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(editChatDto))
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Order(5)
    @SneakyThrows
    void sendMessageTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        UUID firstId = getChatIdByName("first1");
        UUID secondId = getChatIdByName("second");
        UUID thirdId = getChatIdByName("third");
        chatIds.add(firstId);
        chatIds.add(secondId);
        chatIds.add(thirdId);

        for (int i = 0; i < 3; i++) {
            for(String text : messagesChat.get(i)) {
                SendMessageDto dto = new SendMessageDto();
                dto.setChatId(chatIds.get(i));
                dto.setText(text);
                MvcResult result = mockMvc.perform(post(CHAT_SEND_MESSAGE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(dto))
                                .header("Authorization", header))
                        .andExpect(status().isOk()).andReturn();
            }
        }
    }


    @Test
    @Order(6)
    @SneakyThrows
    void getChatInfoTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        UUID first = getChatIdByName("first1");
        MvcResult result = mockMvc.perform(get(CHAT_INFO.replace("{id}", first.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

        ChatInfoDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), ChatInfoDto.class);
        assertEquals(myId, dto.getAdminId());
        assertEquals(otherUsers.get(0), dto.getAvatarId());
        assertEquals("first1", dto.getName());


        result = mockMvc.perform(get(CHAT_INFO.replace("{id}", otherUsers.get(0).toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

        dto = objectMapper.readValue(result.getResponse().getContentAsString(), ChatInfoDto.class);
//        assertEquals(otherUsers.get(0), dto.getAvatarId());
        assertEquals("0 0 0", dto.getName());
    }

    @Test
    @Order(7)
    @SneakyThrows
    void findChatInfoTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        getChatIdByName("first1");
        getChatIdByName("second");
        getChatIdByName("third");
    }

    @Test
    @Order(8)
    @SneakyThrows
    void getMessagesTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        UUID firstId = getChatIdByName("first1");
        UUID secondId = getChatIdByName("second");
        UUID thirdId = getChatIdByName("third");
        chatIds.add(firstId);
        chatIds.add(secondId);
        chatIds.add(thirdId);
        for (int i = 0; i < 3; i++) {
            UUID chatId = chatIds.get(i);
            MvcResult result = mockMvc.perform(get(CHAT_GET_MESSAGES.replace("{id}", chatId.toString()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .header("Authorization", header))
                    .andExpect(status().isOk()).andReturn();

            result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

            List<MessageDto> messages = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
            for (int j = 0; j < messagesChat.get(i).size(); j++) {
                assertEquals(myName, messages.get(j).getAuthorName());
                assertEquals(messagesChat.get(i).get(j), messages.get(j).getText());
            }
        }

        for (int i = 0; i < otherUsers.size(); i++) {
            UUID chatId = otherUsers.get(i);
            MvcResult result = mockMvc.perform(get(CHAT_GET_MESSAGES.replace("{id}", chatId.toString()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .header("Authorization", header))
                    .andExpect(status().isOk()).andReturn();

            result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
            String test = result.getResponse().getContentAsString();

            List<MessageDto> messages = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
            for (int j = 0; j < messagesUsers.get(i).size(); j++) {
                assertEquals(myName, messages.get(j).getAuthorName());
                assertEquals(messagesUsers.get(i).get(j), messages.get(j).getText());
            }
        }
    }

    @Test
    @Order(9)
    @SneakyThrows
    void findMessagesTest() {
        header = "Bearer " + generateAccessToken(myId, "myLogin");
        MvcResult result = mockMvc.perform(get(CHAT_FIND_MESSAGES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("find","Привет")
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        List<MessageFindDto> test = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
        assertEquals(1, test.size());

        result = mockMvc.perform(get(CHAT_FIND_MESSAGES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("find","ну")
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        test = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
        assertEquals(2, test.size());
    }

    @Test
    @Order(10)
    @SneakyThrows
    void getMessagesOtherUserTest() {
        header = "Bearer " + generateAccessToken(otherUsers.get(0), "test");

        UUID firstId = getChatIdByName("first1");
        UUID secondId = getChatIdByName("second");
        UUID thirdId = getChatIdByName("third");
        chatIds.add(firstId);
        chatIds.add(secondId);
        chatIds.add(thirdId);
        for (int i = 0; i < 3; i++) {
            if(i == 1)
            {
                if(secondId == null) {
                    continue;
                }
                else {
                    throw new Exception();
                }
            }

            UUID chatId = chatIds.get(i);
            MvcResult result = mockMvc.perform(get(CHAT_GET_MESSAGES.replace("{id}", chatId.toString()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .header("Authorization", header))
                    .andExpect(status().isOk()).andReturn();

            result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

            List<MessageDto> messages = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
            for (int j = 0; j < messagesChat.get(i).size(); j++) {
                assertEquals(myName, messages.get(j).getAuthorName());
                assertEquals(messagesChat.get(i).get(j), messages.get(j).getText());
            }
        }

        UUID chatId = myId;
        MvcResult result = mockMvc.perform(get(CHAT_GET_MESSAGES.replace("{id}", chatId.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();

        result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        String test = result.getResponse().getContentAsString();

        List<MessageDto> messages = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
        for (int j = 0; j < messagesUsers.get(0).size(); j++) {
            assertEquals(myName, messages.get(j).getAuthorName());
            assertEquals(messagesUsers.get(0).get(j), messages.get(j).getText());
        }

    }

    @Test
    @Order(11)
    @SneakyThrows
    void findMessagesOtherUserTest() {
        header = "Bearer " + generateAccessToken(otherUsers.get(0), "test");
        MvcResult result = mockMvc.perform(get(CHAT_FIND_MESSAGES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("find","Привет")
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        List<MessageFindDto> test = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
        assertEquals(1, test.size());

        result = mockMvc.perform(get(CHAT_FIND_MESSAGES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("find","ну")
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        test = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, MessageDto.class));
        assertEquals(1, test.size());
    }

    private String generateAccessToken(UUID id, String login) {
        var access = Keys.hmacShaKeyFor(Decoders.BASE64.decode("qBTmv4oXFFR2GwjexDJ4t6fsIUIUhhXqlktXjXdkcyygs8nPVEwMfo29VDRRepYDVV5IkIxBMzr7OEHXEHd37w=="));
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(accessExpiration)
                .signWith(access)
                .claim("id", id)
                .compact();
    }

    @SneakyThrows
    private UUID getChatIdByName(String name) {
        ChatQueryDto chatQueryDto = new ChatQueryDto();
        chatQueryDto.setPaginationQueryDto(new PaginationQueryDto());
        chatQueryDto.getPaginationQueryDto().setPageNumber(1);
        chatQueryDto.getPaginationQueryDto().setSize(1);
        chatQueryDto.setNameFilter(name);
        MvcResult result = mockMvc.perform(post(CHAT_FIND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(chatQueryDto))
                        .header("Authorization", header))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        ChatFindInfoPagDto chatFindInfoPagDto = objectMapper.readValue(result.getResponse().getContentAsString(), ChatFindInfoPagDto.class);
        if(chatFindInfoPagDto.getChatFindInfoDtos().size() == 0) {
            return null;
        }
        return chatFindInfoPagDto.getChatFindInfoDtos().get(0).getId();
    }
}
