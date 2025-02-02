package com.api.modules.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUuid(String uuid){
        return userRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("User don't exist"));
    }
}
