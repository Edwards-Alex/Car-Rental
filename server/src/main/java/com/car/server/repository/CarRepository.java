package com.car.server.repository;

import com.car.server.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    //  通过 DBRef 对象的 ID 查询（推荐）
    List<Car> findByOwner_Id(String ownerId);

    List<Car> findByLocationAndAvailableTrue(String location);

    List<Car> findByAvailableTrue();
}
