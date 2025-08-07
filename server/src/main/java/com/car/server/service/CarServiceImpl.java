package com.car.server.service;


import com.car.server.exception.BusinessException;
import com.car.server.exception.enmu.ErrorCode;
import com.car.server.model.Car;
import com.car.server.model.User;
import com.car.server.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService{

    @Autowired
    private CarRepository carRepository;
    @Override
    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> fetchCarsByOwner(String ownerId) {
        List<Car> ownerCars = carRepository.findByOwner_Id(ownerId);
        return ownerCars;
    }

    @Override
    public Car fetchCarById(String id) {
        Optional<Car> carOptional = carRepository.findById(id);
        if(carOptional.isPresent()){
            Car car = carOptional.get();
            return car;
        }else{
            throw new BusinessException(ErrorCode.INVALID_TOKEN.getMessage(), ErrorCode.ACCOUNT_LOCKED.getCode());
        }
    }

    @Override
    public List<Car> findByLocationName(String location) {
        List<Car> cars  = carRepository.findByLocationAndAvailableTrue(location);
        return cars;
    }

    @Override
    public List<Car> findCars() {
        return carRepository.findByAvailableTrue();
    }
}
