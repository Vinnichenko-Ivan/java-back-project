package com.hits.friends;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.hits.common.dto.user.FullNameDto;
import com.hits.common.dto.user.PaginationQueryDto;
import com.hits.common.exception.NotImplementedException;
import com.hits.friends.dto.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.swagger.models.auth.In;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.hits.common.Paths.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendsApplicationTests {

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
	@SneakyThrows
	void beforeEach(){
		server = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8081));
		server.start();

		myId = UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648a");
		myName = new FullNameDto();
		myName.setName("my1");
		myName.setPatronymic("my2");
		myName.setSurname("my3");
		blocking = List.of(
				UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648b"),
				UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648c"),
				UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da648d")
		);
		names = new ArrayList<>();
		header = "Bearer " + generateAccessToken(myId, "myLogin");
		Integer counter = 0;
		for(UUID id : blocking) {
			FullNameDto dto = new FullNameDto();
			dto.setSurname(counter.toString());
			dto.setName(counter.toString());
			dto.setPatronymic(counter.toString());
			counter+=1;
			names.add(dto);

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



		friends = List.of(
				UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da647b"),
				UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da647c"),
				UUID.fromString("f3714bcb-792e-48c7-ba64-6f8779da647d")
		);
		names2 = new ArrayList<>();
		for(UUID id : friends) {
			FullNameDto dto = new FullNameDto();
			dto.setSurname(counter.toString());
			dto.setName(counter.toString());
			dto.setPatronymic(counter.toString());
			counter+=1;
			names2.add(dto);

			server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/users/common/check/" + id.toString())
					.willReturn(ok()
							.withHeader("Content-Type", "application/json")
							.withBody("true")));
			server.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get("/users/common/user/" + id.toString())
					.willReturn(ok()
							.withHeader("Content-Type", "application/json")
							.withBody(objectMapper.writeValueAsString(dto))));
		}
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
	private UUID myId;
	private FullNameDto myName;
	private List<UUID> blocking;
	private List<FullNameDto> names;

	private List<UUID> friends;
	private List<FullNameDto> names2;
	private String header;
	@Test
	@Order(1)
	void contextLoads() {
		log.info("context load");
	}
	@Test
	@Order(2)
	@SneakyThrows
	void addBlockingTest() {
		for (int i = 0; i < 3; i++) {
			AddRelationDto addRelationDto = new AddRelationDto();
			addRelationDto.setFullName(names.get(i));
			addRelationDto.setTargetId(blocking.get(i));

			MvcResult result = mockMvc.perform(post(FRIEND_BLOCKING_ADD)
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.header("Authorization", header)
							.content(objectMapper.writeValueAsString(addRelationDto)))
					.andExpect(status().isOk()).andReturn();
		}
	}
	@Test
	@Order(3)
	@SneakyThrows
	void getBlockTest() {
		for (int i = 0; i < 3; i++) {
			MvcResult result = mockMvc.perform(get(FRIEND_BLOCKING_GET_BLOCK.replace("{id}", blocking.get(i).toString()))
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.header("Authorization", header))
					.andExpect(status().isOk()).andReturn();
		}
	}
	@Test
	@Order(4)
	@SneakyThrows
	void getBlockingsTest() {
		QueryRelationDto queryRelationDto = new QueryRelationDto();
		PaginationQueryDto paginationQueryDto = new PaginationQueryDto();
		paginationQueryDto.setSize(10);
		paginationQueryDto.setPageNumber(1);
		QueryRelationFilter queryRelationFilter = new QueryRelationFilter();
		queryRelationFilter.setName("");
		queryRelationFilter.setPatronymic("");
		queryRelationFilter.setSurname("");
		QueryRelationSort queryRelationSort = new QueryRelationSort();

		queryRelationDto.setQueryRelationFilter(queryRelationFilter);
		queryRelationDto.setQueryRelationSort(queryRelationSort);
		queryRelationDto.setPaginationQueryDto(paginationQueryDto);



		var result = mockMvc.perform(post(FRIEND_BLOCKING_GET_BLOCKING)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(queryRelationDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();

		RelationsDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), RelationsDto.class);

		assertEquals(dto.getPaginationDto().getSize(), 3);
	}

	@Test
	@Order(5)
	@SneakyThrows
	void deleteBlockingTest() {
		MvcResult result = mockMvc.perform(delete(FRIEND_BLOCKING_DELETE.replace("{id}", blocking.get(2).toString()))
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.header("Authorization", header))
				.andExpect(status().isOk()).andReturn();


		QueryRelationDto queryRelationDto = new QueryRelationDto();
		PaginationQueryDto paginationQueryDto = new PaginationQueryDto();
		paginationQueryDto.setSize(10);
		paginationQueryDto.setPageNumber(1);
		QueryRelationFilter queryRelationFilter = new QueryRelationFilter();
		queryRelationFilter.setName("");
		queryRelationFilter.setPatronymic("");
		queryRelationFilter.setSurname("");
		QueryRelationSort queryRelationSort = new QueryRelationSort();

		queryRelationDto.setQueryRelationFilter(queryRelationFilter);
		queryRelationDto.setQueryRelationSort(queryRelationSort);
		queryRelationDto.setPaginationQueryDto(paginationQueryDto);



		result = mockMvc.perform(post(FRIEND_BLOCKING_GET_BLOCKING)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(queryRelationDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();

		RelationsDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), RelationsDto.class);

		assertEquals(dto.getPaginationDto().getSize(), 2);
	}
	@Test
	@Order(6)
	@SneakyThrows
	void findBlockingTest() {
		MvcResult result = mockMvc.perform(get(FRIEND_BLOCKING_CHECK.replace("{id}", blocking.get(0).toString()))
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		Boolean answer = objectMapper.readValue(result.getResponse().getContentAsString(), Boolean.class);
		assertEquals(answer, true);

		result = mockMvc.perform(get(FRIEND_BLOCKING_CHECK.replace("{id}", blocking.get(2).toString()))
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.header("Authorization", header))
				.andExpect(status().isOk()).andReturn();
		answer = objectMapper.readValue(result.getResponse().getContentAsString(), Boolean.class);
		assertEquals(answer, false);
	}
	@Test
	@Order(7)
	@SneakyThrows
	void checkBlockingTest() {

	}

	@Test
	@Order(8)
	@SneakyThrows
	void addFriendTest() {
		for (int i = 0; i < 3; i++) {
			AddRelationDto addRelationDto = new AddRelationDto();
			addRelationDto.setFullName(names2.get(i));
			addRelationDto.setTargetId(friends.get(i));

			MvcResult result = mockMvc.perform(post(FRIEND_FRIENDSHIP_ADD)
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.header("Authorization", header)
							.content(objectMapper.writeValueAsString(addRelationDto)))
					.andExpect(status().isOk()).andReturn();
		}
	}
	@Test
	@Order(9)
	@SneakyThrows
	void getFriendTest() {
		for (int i = 0; i < 3; i++) {
			MvcResult result = mockMvc.perform(get(FRIEND_FRIENDSHIP_GET_FRIEND.replace("{id}", friends.get(i).toString()))
							.contentType(MediaType.APPLICATION_JSON)
							.characterEncoding(StandardCharsets.UTF_8)
							.header("Authorization", header))
					.andExpect(status().isOk()).andReturn();
		}
	}
	@Test
	@Order(10)
	@SneakyThrows
	void getFriendsTest() {
		QueryRelationDto queryRelationDto = new QueryRelationDto();
		PaginationQueryDto paginationQueryDto = new PaginationQueryDto();
		paginationQueryDto.setSize(10);
		paginationQueryDto.setPageNumber(1);
		QueryRelationFilter queryRelationFilter = new QueryRelationFilter();
		queryRelationFilter.setName("");
		queryRelationFilter.setPatronymic("");
		queryRelationFilter.setSurname("");
		QueryRelationSort queryRelationSort = new QueryRelationSort();

		queryRelationDto.setQueryRelationFilter(queryRelationFilter);
		queryRelationDto.setQueryRelationSort(queryRelationSort);
		queryRelationDto.setPaginationQueryDto(paginationQueryDto);



		MvcResult result = mockMvc.perform(post(FRIEND_FRIENDSHIP_GET_FRIENDS)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(queryRelationDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();

		RelationsDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), RelationsDto.class);

		assertEquals(dto.getPaginationDto().getSize(), 3);
	}

	@Test
	@Order(11)
	@SneakyThrows
	void deleteFriendTest() {
		MvcResult result = mockMvc.perform(delete(FRIEND_FRIENDSHIP_DELETE.replace("{id}", friends.get(2).toString()))
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.header("Authorization", header))
				.andExpect(status().isOk()).andReturn();


		QueryRelationDto queryRelationDto = new QueryRelationDto();
		PaginationQueryDto paginationQueryDto = new PaginationQueryDto();
		paginationQueryDto.setSize(10);
		paginationQueryDto.setPageNumber(1);
		QueryRelationFilter queryRelationFilter = new QueryRelationFilter();
		queryRelationFilter.setName("");
		queryRelationFilter.setPatronymic("");
		queryRelationFilter.setSurname("");
		QueryRelationSort queryRelationSort = new QueryRelationSort();

		queryRelationDto.setQueryRelationFilter(queryRelationFilter);
		queryRelationDto.setQueryRelationSort(queryRelationSort);
		queryRelationDto.setPaginationQueryDto(paginationQueryDto);



		result = mockMvc.perform(post(FRIEND_FRIENDSHIP_GET_FRIENDS)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(queryRelationDto))
						.characterEncoding(StandardCharsets.UTF_8).header("Authorization", header))
				.andExpect(status().isOk()).andReturn();

		RelationsDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), RelationsDto.class);

		assertEquals(dto.getPaginationDto().getSize(), 2);
	}
	@Test
	@Order(12)
	@SneakyThrows
	void findFriendTest() {

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
