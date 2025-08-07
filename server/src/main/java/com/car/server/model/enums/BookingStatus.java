package com.car.server.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookingStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),

    CANCELLED("cancelled");


    private final String bookingStatus;

    //构造函数
    BookingStatus(String bookingStatus){this.bookingStatus = bookingStatus;}

    public String getBookingStatus() {
        return bookingStatus;
    }

}
