package de.unibayreuth.se.taskboard.business.ports;

import java.util.List;
import java.util.Optional;
import de.unibayreuth.se.taskboard.business.domain.User;

public interface UserService {
   

   List<User> getAllUsers();

   Optional<User> getUserById(String id);
   
   void createUser(User user);
    //TODO: Add user service interface that the controller uses to interact with the business layer.
    //TODO: Implement the user service interface in the business layer, using the existing user persistence service.
}  
