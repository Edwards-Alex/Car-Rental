package com.car.server.service;

import com.car.server.exception.BusinessException;
import com.car.server.exception.enmu.ErrorCode;
import com.car.server.model.User;
import com.car.server.repository.UserRepository;
import com.car.server.utility.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        //encode password then save
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String email, String password){
        //search for user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND.getCode());
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD.getMessage(), ErrorCode.INVALID_PASSWORD.getCode());
        }

        return user;
    }

    @Override
    public User findById(String id) {
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return user;
        }else{
            throw new BusinessException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.ACCOUNT_LOCKED.getCode());
        }
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
