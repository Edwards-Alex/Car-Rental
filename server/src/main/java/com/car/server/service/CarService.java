package com.car.server.service;

import com.car.server.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarService {
    public Car addCar(Car car);

    public List<Car> fetchCarsByOwner(String ownerId);

    public Car fetchCarById(String id);

    public List<Car> findByLocationName(String location);

    public List<Car> findCars();
}
