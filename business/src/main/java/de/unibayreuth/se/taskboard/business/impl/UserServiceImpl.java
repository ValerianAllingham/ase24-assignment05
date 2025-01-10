package de.unibayreuth.se.taskboard.business.impl;

import de.unibayreuth.se.taskboard.business.domain.User;
import de.unibayreuth.se.taskboard.business.exceptions.MalformedRequestException;
import de.unibayreuth.se.taskboard.business.exceptions.UserNotFoundException;
import de.unibayreuth.se.taskboard.business.ports.UserPersistenceService;
import de.unibayreuth.se.taskboard.business.ports.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
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
    public void clear() {
        userPersistenceService.clear();
    }

    @Override
    @NonNull
    public List<User> getAllUsers() {
        return userPersistenceService.getAll();
    }

    @Override
    @NonNull
    public Optional<User> getById(@NonNull UUID id) {
        try {

            return userPersistenceService.getById(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    @NonNull
    public User createUser(@NonNull User user) throws MalformedRequestException, UserNotFoundException {
        if (user.getId() != null) {
            throw new MalformedRequestException("Task ID must not be set.");
        }
        return upsert(user);
    }

    @Override
    @NonNull
    public User upsert(@NonNull User user) {
        if (user.getId() != null) {
            verifyUserExists(user.getId());
        }
        return userPersistenceService.upsert(user);

    }
    @NonNull
    private void verifyUserExists(@NonNull UUID id) throws UserNotFoundException {
        userPersistenceService.getById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " does not exist."));
    }

}
