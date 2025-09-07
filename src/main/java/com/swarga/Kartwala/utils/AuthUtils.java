package com.swarga.Kartwala.utils;

import com.swarga.Kartwala.exception.UserNotFoundException;
import com.swarga.Kartwala.model.User;
import com.swarga.Kartwala.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    @Autowired
    private UserRepository userRepository;

    public String loggedInEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException("User not available for username: "+authentication.getName())
        );
        return user.getEmail();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException("User not available for username: "+authentication.getName())
        );
    }

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user= userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new UserNotFoundException("User not available for username: "+authentication.getName())
        );
        return user.getUserId();
    }
}
