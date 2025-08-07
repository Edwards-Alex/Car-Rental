package com.car.server.service;

import com.car.server.model.Booking;
import com.car.server.model.enums.BookingStatus;
import com.car.server.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public boolean checkAvailability(String carId, LocalDate pickupDate, LocalDate returnDate) {
            //when return long less or equal to 0, query result unavailable >0 so available is == 0
            return bookingRepository.countConflictingBookings(carId,pickupDate,returnDate) == 0;
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findUserBookings(String userId) {

        return bookingRepository.findBookingsByUserOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Booking> findOwnerBookings(String ownerId) {
        return bookingRepository.findBookingsByOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    @Override
    public Booking findByBookingId(String bookingId) {
        Optional<Booking> optionalBooking =  bookingRepository.findById(bookingId);
       if(optionalBooking.isPresent()){
           Booking booking = optionalBooking.get();
           return  booking;

       }else{
           // 处理不存在的逻辑
           throw new RuntimeException("Booking not found with id: " + bookingId);
       }
    }

    @Override
    public List<Booking> findByOwnerIdAndStatus(String ownerId, BookingStatus status) {
        return bookingRepository.findByOwnerIdAndStatus(ownerId,status);
    }
}
