package com.car.server.model.DTO;

import java.time.LocalDate;
import java.util.Date;

public class BookingCarRequest {
    private LocalDate pickupDate;
    private LocalDate  returnDate;

    private String location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
