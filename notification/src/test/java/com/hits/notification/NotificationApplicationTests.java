package com.hits.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hits.common.dto.notification.CreateNotificationDto;
import com.hits.common.enums.NotificationType;
import com.hits.notification.config.RabbitConfig;
import com.hits.notification.dto.NotificationFilterDto;
import com.hits.notification.dto.NotificationsDto;
import com.hits.notification.dto.NotificationsQueryDto;
import com.hits.notification.dto.ReadDto;
import com.hits.notification.model.NotificationNotReadDto;
import com.hits.notification.model.NotificationStatus;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.hits.common.Paths.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(RabbitConfig.class)
class NotificationApplicationTests {
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

	private UUID myId = UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da647a");
	private UUID notMyId = UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da647c");

	private String header;

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

	@BeforeEach
	void beforeEach() {
		header = "Bearer " + generateAccessToken(myId, "myLogin");
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

	@SneakyThrows
	@Test
	@Order(1)
	void contextLoads() {
	}

	@SneakyThrows
	@Test
	@Order(2)
	void addNotifications() {
		List<NotificationType> types = List.of(
				NotificationType.LOGIN,
				NotificationType.BLOCK_DELETE,
				NotificationType.FRIEND_DELETE,
				NotificationType.BLOCK_DELETE,
				NotificationType.FRIEND_DELETE,
				NotificationType.LOGIN,
				NotificationType.NEW_MESSAGE,
				NotificationType.NEW_MESSAGE,
				NotificationType.NEW_MESSAGE,
				NotificationType.BLOCK_ADD,
				NotificationType.FRIEND_ADD,
				NotificationType.LOGIN
		);

		List<String> texts = List.of(
				"NotificationType.LOGIN",
				"NotificationType.BLOCK_DELETE",
				"NotificationType.FRIEND_DELETE",
				"NotificationType.BLOCK_DELETE",
				"NotificationType.FRIEND_DELETE",
				"NotificationType.LOGIN",
				"NotificationType.NEW_MESSAGE",
				"NotificationType.NEW_MESSAGE",
				"NotificationType.NEW_MESSAGE",
				"NotificationType.BLOCK_ADD",
				"NotificationType.FRIEND_ADD",
				"NotificationType.LOGIN"
		);

		for (int i = 0; i < texts.size(); i++) {
			CreateNotificationDto createNotificationDto = new CreateNotificationDto();
			createNotificationDto.setNotificationType(types.get(i));
			createNotificationDto.setText(texts.get(i));
			if(i == texts.size() - 1)
			{
				createNotificationDto.setUserId(notMyId);
			}
			else {
				createNotificationDto.setUserId(myId);
			}
			rabbitTemplate.convertAndSend("standart_notify","", createNotificationDto);
		}
	}

	@SneakyThrows
	@Test
	@Order(3)
	void checkNotificationTest() {
		Thread.sleep(5000);
		var result = mockMvc.perform(get(NOTIFICATION_NOT_READ)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();

		Long count = objectMapper.readValue(result.getResponse().getContentAsString(), Long.class);
		assertEquals(3, count);
	}

	@SneakyThrows
	@Test
	@Order(4)
	void getNotificationTest() {
		NotificationsQueryDto notificationsQueryDto = new NotificationsQueryDto();
		NotificationFilterDto notificationFilterDto = new NotificationFilterDto();
		notificationFilterDto.setFilter("");
		notificationFilterDto.setFilterType(Set.of(NotificationType.NEW_MESSAGE));
		notificationsQueryDto.setNotificationFilterDto(notificationFilterDto);
		var result = mockMvc.perform(post(NOTIFICATION_GET)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(notificationsQueryDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		NotificationsDto notificationsDto = objectMapper.readValue(result.getResponse().getContentAsString(), NotificationsDto.class);
		assertEquals(3, notificationsDto.getNotifications().size());
		assertEquals(3, notificationsDto.getPaginationDto().getSize());


		notificationFilterDto.setFilter("");
		notificationFilterDto.setFilterType(Set.of(NotificationType.LOGIN));
		notificationsQueryDto.setNotificationFilterDto(notificationFilterDto);
		result = mockMvc.perform(post(NOTIFICATION_GET)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(notificationsQueryDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		notificationsDto = objectMapper.readValue(result.getResponse().getContentAsString(), NotificationsDto.class);
		assertEquals(2,notificationsDto.getNotifications().size());
		assertEquals(2, notificationsDto.getPaginationDto().getSize());

		notificationFilterDto.setFilter("");
		notificationFilterDto.setFilterType(Set.of(NotificationType.BLOCK_ADD, NotificationType.BLOCK_DELETE));
		notificationsQueryDto.setNotificationFilterDto(notificationFilterDto);
		result = mockMvc.perform(post(NOTIFICATION_GET)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(notificationsQueryDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		notificationsDto = objectMapper.readValue(result.getResponse().getContentAsString(), NotificationsDto.class);
		assertEquals(3, notificationsDto.getNotifications().size());
		assertEquals(3, notificationsDto.getPaginationDto().getSize());

		notificationFilterDto.setFilter("BLOCK_ADD");
		notificationFilterDto.setFilterType(Set.of(NotificationType.BLOCK_ADD, NotificationType.BLOCK_DELETE));
		notificationsQueryDto.setNotificationFilterDto(notificationFilterDto);
		result = mockMvc.perform(post(NOTIFICATION_GET)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(notificationsQueryDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		notificationsDto = objectMapper.readValue(result.getResponse().getContentAsString(), NotificationsDto.class);
		assertEquals(1, notificationsDto.getNotifications().size());
		assertEquals(1, notificationsDto.getPaginationDto().getSize());
	}

	@SneakyThrows
	@Test
	@Order(5)
	void checkEditTest() {
		NotificationsQueryDto notificationsQueryDto = new NotificationsQueryDto();
		NotificationFilterDto notificationFilterDto = new NotificationFilterDto();
		notificationFilterDto.setFilter("");
		notificationFilterDto.setFilterType(Set.of(NotificationType.NEW_MESSAGE));
		notificationsQueryDto.setNotificationFilterDto(notificationFilterDto);
		var result = mockMvc.perform(post(NOTIFICATION_GET)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(notificationsQueryDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		NotificationsDto notificationsDto = objectMapper.readValue(result.getResponse().getContentAsString(), NotificationsDto.class);

		UUID first = notificationsDto.getNotifications().get(0).getId();
		UUID second = notificationsDto.getNotifications().get(1).getId();
		UUID third = notificationsDto.getNotifications().get(2).getId();

		ReadDto readDto = new ReadDto();
		readDto.setStatus(NotificationStatus.READ);
		readDto.setIds(List.of(first));

		result = mockMvc.perform(post(NOTIFICATION_READ)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(readDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();

		readDto = new ReadDto();
		readDto.setStatus(NotificationStatus.SEND);
		readDto.setIds(List.of(second, third));
		result = mockMvc.perform(post(NOTIFICATION_READ)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(readDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();

		result = mockMvc.perform(post(NOTIFICATION_GET)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(notificationsQueryDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		result.getResponse().setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
		notificationsDto = objectMapper.readValue(result.getResponse().getContentAsString(), NotificationsDto.class);
		assertEquals(3, notificationsDto.getNotifications().size());
		assertEquals(3, notificationsDto.getPaginationDto().getSize());

		for (var notification : notificationsDto.getNotifications()) {
			if(notification.getId().equals(first)){
				assertEquals(notification.getStatus(), NotificationStatus.READ);
				assertNotNull(notification.getReadDate());
			}
			else if(notification.getId().equals(second)){
				assertEquals(notification.getStatus(), NotificationStatus.SEND);
				assertNull(notification.getReadDate());
			}
			else if(notification.getId().equals(third)){
				assertEquals(notification.getStatus(), NotificationStatus.SEND);
				assertNull(notification.getReadDate());
			}
			else {
				throw new Exception();
			}
		}
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
}
