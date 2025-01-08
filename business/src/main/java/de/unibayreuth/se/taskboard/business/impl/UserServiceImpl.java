package de.unibayreuth.se.taskboard.business.impl;

import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.exceptions.DuplicateNameException;
import de.unibayreuth.se.taskboard.business.exceptions.UserNotFoundException;
import de.unibayreuth.se.taskboard.business.ports.UserPersistenceService;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserService that uses UserPersistenceService for persistence logic.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserPersistenceService userPersistenceService;

    @Override
    public List<User> getAllUsers() {
        return userPersistenceService.getAll();
    }

    @Override
    public Optional<User> getUserById(String id) {
        try {
            UUID uuid = UUID.fromString(id); // Convert String to UUID
            return userPersistenceService.getById(uuid);
        } catch (IllegalArgumentException e) {
            return Optional.empty(); // Return empty Optional if the ID is invalid
        }
    }

    @Override
    public void createUser(User user) {
        try {
            userPersistenceService.upsert(user); // Delegate to UserPersistenceService
        } catch (DuplicateNameException | UserNotFoundException e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }
}
