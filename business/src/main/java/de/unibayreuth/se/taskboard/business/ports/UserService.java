package de.unibayreuth.se.taskboard.business.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.unibayreuth.se.taskboard.business.domain.User;
import org.springframework.lang.NonNull;

public interface UserService {
   
   void clear();
   @NonNull
   List<User> getAllUsers();
   @NonNull
   Optional<User> getById(@NonNull UUID id);
   @NonNull
   User createUser(@NonNull User user);
   @NonNull
   User upsert (@NonNull User user);
    //TODO: Add user service interface that the controller uses to interact with the business layer.
    //TODO: Implement the user service interface in the business layer, using the existing user persistence service.
}  
