package com.car.server.model.DTO;

import com.car.server.model.Car;

import java.time.LocalDate;
import java.util.Date;


public class BookingRequest {
    private Car car;
    private LocalDate pickupDate;
    private LocalDate returnDate;

    public BookingRequest(Car car, LocalDate pickupDate, LocalDate returnDate) {
        this.car = car;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
