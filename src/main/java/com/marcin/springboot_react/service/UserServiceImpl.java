package com.marcin.springboot_react.service;

import com.marcin.springboot_react.model.User;
import com.marcin.springboot_react.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers(User user) {
        log.info("Fetching list of all users, invoked by: '{}'", user);
        return this.userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        log.info("About to load user by username: '{}'", username);
        Optional<User> user = this.userRepository.findByUsername(username);

        if (user.isEmpty()) {
            log.error("Could not find the user with username: '{}'", username);
            throw new UsernameNotFoundException(username);
        }

        log.info("Found the user: '{}'", username);
        return user.get();
    }

    @Override
    public User updateUser(User modifiedUser) {
        log.info("Updating user data: '{}'", modifiedUser);
        User userInDb = this.userRepository.findById(modifiedUser.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Failed to find the user: '{}' in the database",
                                modifiedUser.getUserId())));

        userInDb.setUserLevel(modifiedUser.getUserLevel());
        userInDb.setUsername(modifiedUser.getUsername());

        if (modifiedUser.getPassword().length() > 0) {
            userInDb.setPassword(modifiedUser.getPassword());
        }

        return this.userRepository.save(userInDb);
    }

    @Override
    public void deleteUser(User userToDelete) {
        this.userRepository.delete(userToDelete);
        log.info("Deleted user: '{}'", userToDelete.getUsername());
    }
}
