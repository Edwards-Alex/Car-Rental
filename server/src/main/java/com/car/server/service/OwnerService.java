package com.car.server.service;

import org.springframework.stereotype.Service;

@Service
public interface OwnerService {
    public boolean updateRoleToOwner(String id);
}
