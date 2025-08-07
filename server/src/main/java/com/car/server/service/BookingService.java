package com.car.server.service;

import com.car.server.model.Booking;
import com.car.server.model.Car;
import com.car.server.model.User;
import com.car.server.model.enums.BookingStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public interface BookingService {
    public boolean checkAvailability(String carId, LocalDate pickupDate, LocalDate returnDate);

    public Booking save(Booking booking);

    public Booking findByBookingId(String bookingId);

    public List<Booking> findUserBookings(String userId);

    public List<Booking> findOwnerBookings(String ownerId);

    public List<Booking> findByOwnerIdAndStatus(String ownerId, BookingStatus status);
}
