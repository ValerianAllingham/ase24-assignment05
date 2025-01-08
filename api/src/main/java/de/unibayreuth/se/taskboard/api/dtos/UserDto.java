package de.unibayreuth.se.taskboard.api.dtos;

import java.time.LocalDateTime;

//TODO: Add DTO for users.
public record UserDto( String id, LocalDateTime createdAt, String name) { }
