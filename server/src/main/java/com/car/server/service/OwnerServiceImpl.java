package com.car.server.service;

import com.car.server.exception.BusinessException;
import com.car.server.exception.enmu.ErrorCode;
import com.car.server.model.User;
import com.car.server.model.enums.UserRole;
import com.car.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerServiceImpl  implements OwnerService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean updateRoleToOwner(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setRole(UserRole.OWNER);
            userRepository.save(user);
            return true;
        }else{
            throw new BusinessException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.ACCOUNT_LOCKED.getCode());
        }
    }
}
