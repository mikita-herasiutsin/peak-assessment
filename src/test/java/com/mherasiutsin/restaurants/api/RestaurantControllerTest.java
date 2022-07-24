package com.mherasiutsin.restaurants.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mherasiutsin.restaurants.MysqlTestContainer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.MySQLContainer;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.databind.node.JsonNodeType.ARRAY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfiguration.class})
public class RestaurantControllerTest {

    @ClassRule
    public static MySQLContainer<MysqlTestContainer> mysqlSQLContainer = MysqlTestContainer.getInstance();
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnRestaurants() throws JsonProcessingException {
        List<String> expectedGrades = List.of("A", "B");
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/restaurants?page=1&size=20&q=Chinese&grade=A&grade=B",
                HttpMethod.GET,
                new HttpEntity<>(null, null),
                String.class
        );
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        String body = response.getBody();
        assertThat(body, notNullValue());

        JsonNode jsonNode = objectMapper.readTree(body);
        assertThat(jsonNode.get("items").getNodeType(), is(ARRAY));

        List<JsonNode> items = new ArrayList<>();
        jsonNode.get("items").elements().forEachRemaining(items::add);
        assertThat(items.size(), is(9));
        assertThat(
                items.stream()
                        .filter(i -> expectedGrades.contains(i.get("grade").asText()))
                        .count(),
                is(9L));
        assertThat(
                items.stream()
                        .filter(i -> i.get("cuisineDescription").asText().toLowerCase().contains("chinese")
                                             || i.get("dba").asText().toLowerCase().contains("chinese"))
                        .count(),
                is(9L));
    }

    @Test
    public void shouldReturnRestaurantFilters() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/restaurants/filters",
                HttpMethod.GET,
                new HttpEntity<>(null, null),
                String.class
        );
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        String body = response.getBody();
        assertThat(body, notNullValue());

        JsonNode bodyNode = objectMapper.readTree(body);
        assertThat(bodyNode.get("cuisines").getNodeType(), is(ARRAY));
        assertThat(bodyNode.get("grades").getNodeType(), is(ARRAY));

        List<JsonNode> cuisines = new ArrayList<>();
        bodyNode.get("cuisines").elements().forEachRemaining(cuisines::add);
        assertThat(cuisines.size(), is(48));

        List<JsonNode> grades = new ArrayList<>();
        bodyNode.get("grades").elements().forEachRemaining(grades::add);
        assertThat(grades.size(), is(5));
    }

}
