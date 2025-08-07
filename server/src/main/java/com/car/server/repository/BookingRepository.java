package com.car.server.repository;

import com.car.server.model.Booking;
import com.car.server.model.enums.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {



    @Query(value = """
    {
        'car.$id': { '$eq': ObjectId(?0) },
        'pickupDate': { '$lte': ?2 },
        'returnDate': { '$gte': ?1 },
        'status': { '$ne': 'CANCELLED' }
    }
    """, count = true)
    long countConflictingBookings(  // 返回 long，不会为 null
        String carId,
        LocalDate pickupDate,
        LocalDate returnDate
    );


   /* @Query("""
        {
            'car.$id': ?0,
            'pickUpDate': { "$lte": ?2 },
            'returnDate': { "$gte": ?1 },
            'status': { "$ne": 'CANCELLED' }
        }
        """)
    boolean existsConflictingBooking(
            String carId,
            LocalDate pickupDate,
            LocalDate returnDate
    );*/



    List<Booking> findBookingsByUserOrderByCreatedAtDesc(String userId);


    List<Booking> findBookingsByOwnerIdOrderByCreatedAtDesc (String ownerId);

    List<Booking> findByOwnerIdAndStatus(String ownerId, BookingStatus status);
}
