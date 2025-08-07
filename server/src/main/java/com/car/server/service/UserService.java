package com.car.server.service;

import com.car.server.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public User registerUser (User user);

    public User loginUser(String email, String password);

    public User  findById(String id);

    public User save(User user);
}
