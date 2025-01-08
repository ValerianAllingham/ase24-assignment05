package de.unibayreuth.se.taskboard.api.mapper;

import de.unibayreuth.se.taskboard.api.dtos.UserDto;
import de.unibayreuth.se.taskboard.business.domain.User;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    default UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
            user.getId() != null ? user.getId().toString() : null,
            user.getCreatedAt(),
            user.getName()
            
        );
    }

    default User toDomain(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        // Create a User object with the name
        User user = new User(userDto.name());

        // Set additional fields manually
        user.setId(userDto.id() != null ? UUID.fromString(userDto.id()) : null);
        user.setCreatedAt(userDto.createdAt() != null ? userDto.createdAt() : LocalDateTime.now(ZoneId.of("UTC")));
        // If timezone is part of the DTO


        return user;
    }

}
