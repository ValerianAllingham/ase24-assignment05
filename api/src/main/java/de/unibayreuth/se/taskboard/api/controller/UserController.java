package de.unibayreuth.se.taskboard.api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.unibayreuth.se.taskboard.business.ports.UserService;
import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.api.mapper.UserDtoMapper;


import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@OpenAPIDefinition(
        info = @Info(
                title = "TaskBoard",
                version = "0.0.1"
        )
)
@Tag(name = "Users")
@RestController // Change to @RestController since it's handling REST endpoints
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    // TODO: Add GET /api/users endpoint to retrieve all users.
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(userDtoMapper::fromBusiness) // Convert User -> UserDto
                .collect(Collectors.toList());
        return ResponseEntity.ok(users); // Return 200 OK with the list of users
    }



    // TODO: Add GET /api/users/{id} endpoint to retrieve a user by ID.
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable UUID id) {
        return userService.getById(id)
                .map(user -> ResponseEntity.ok(userDtoMapper.fromBusiness(user))) // Map User -> UserDto
                .orElse(ResponseEntity.notFound().build()); // Return 404 if user not found
    }

    // TODO: Add POST /api/users endpoint to create a new user based on a provided user DTO.
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto dto) {
    // Map UserDto to User
    User user = userDtoMapper.toBusiness(dto);
    User createdUser = userService.createUser(user);
        return ResponseEntity.created(URI.create("/api/users/" + createdUser.getId())).build();
    }

}
