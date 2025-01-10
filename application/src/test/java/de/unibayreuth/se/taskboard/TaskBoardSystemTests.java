package de.unibayreuth.se.taskboard;

import de.unibayreuth.se.taskboard.api.dtos.TaskDto;
import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.api.mapper.TaskDtoMapper;
import de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper;
import de.unibayreuth.se.taskboard.business.domain.Task;
import de.unibayreuth.se.taskboard.business.domain.User;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class TaskBoardSystemTests extends AbstractSystemTest {

    @Autowired
    private TaskDtoMapper taskDtoMapper;
    @Autowired
    private UserDtoMapper userDtoMapper;
    @Test
    void getAllCreatedTasks() {
        List<Task> createdTasks = TestFixtures.createTasks(taskService);

        List<Task> retrievedTasks = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/tasks")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdTasks.size()))
                .and()
                .extract().jsonPath().getList("$", TaskDto.class)
                .stream()
                .map(taskDtoMapper::toBusiness)
                .toList();

        assertThat(retrievedTasks)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .containsExactlyInAnyOrderElementsOf(createdTasks);
    }

    @Test
    void createAndDeleteTask() {
        Task createdTask = taskService.create(
                TestFixtures.TASKS.getFirst()
        );

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .delete("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(200);

        when()
                .get("/api/tasks/{id}", createdTask.getId())
                .then()
                .statusCode(400);

    }

    @Test
    void getUserById() {
        // Create a user via the create endpoint
        String createdUserId = given()
                .contentType(ContentType.JSON)
                .body(new UserDto(null, LocalDateTime.now(), "Test User"))
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract()
                .header("Location").split("/api/users/")[1];


        // Fetch the user by ID
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/{id}", createdUserId)
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(createdUserId))
                .body("name", Matchers.equalTo("Test User"));
    }

    @Test
    void getAllUsers() {
        // Create multiple users via the create endpoint
        List<String> createdUserIds = List.of(
                given()
                        .contentType(ContentType.JSON)
                        .body(new UserDto(null, LocalDateTime.now(), "Test User1"))
                        .when()
                        .post("/api/users")
                        .then()
                        .statusCode(201)
                        .extract()
                        .header("Location").split("/api/users/")[1],

                given()
                        .contentType(ContentType.JSON)
                        .body(new UserDto(null, LocalDateTime.now(), "Test User2"))
                        .when()
                        .post("/api/users")
                        .then()
                        .statusCode(201)
                        .extract()
                        .header("Location").split("/api/users/")[1]
        );

        // Retrieve all users via the API
        List<User> retrievedUsers = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(createdUserIds.size())) // Ensure the number of users matches
                .and()
                .extract().jsonPath().getList("$", UserDto.class)
                .stream()
                .map(userDtoMapper::toBusiness)
                .toList();

        // Validate the retrieved users match the created ones
        assertThat(retrievedUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt") // Ignore timestamps
                .allMatch(user -> {
                    assert user.getId() != null;
                    return createdUserIds.contains(user.getId().toString());
                });
    }


    //TODO: Add at least one test for each new endpoint in the users controller (the create endpoint can be tested as part of the other endpoints).
}