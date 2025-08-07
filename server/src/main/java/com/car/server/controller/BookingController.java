package com.car.server.controller;

import com.car.server.model.Booking;
import com.car.server.model.Car;
import com.car.server.model.DTO.BookingCarRequest;
import com.car.server.model.DTO.BookingRequest;
import com.car.server.model.User;
import com.car.server.model.enums.BookingStatus;
import com.car.server.service.BookingService;
import com.car.server.service.CarService;
import com.car.server.service.UserService;
import com.car.server.utility.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //Function to Check Availability of Car for a given Date
    @PostMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(@RequestBody BookingRequest request){
        Car car = request.getCar();
        LocalDate pickupDate = request.getPickupDate();
        LocalDate returnDate = request.getReturnDate();

        boolean flag = bookingService.checkAvailability(car.getId(),pickupDate,returnDate);

        if(!flag){
            Map<String,Object> res = new HashMap<>();
            res.put("success",false);
            res.put("message","car is booking,please check another car" );
            return ResponseEntity.ok(res);
        }
            Map<String,Object> res = new HashMap<>();
            res.put("success",true);
            res.put("message","booking availability");
            return ResponseEntity.ok(res);
    }

    //API to Check Availability of Cars for the given Date and location
    @PostMapping("/check-availability-location")
    public ResponseEntity<?> checkAvailabilityOfCar (@RequestBody BookingCarRequest request){
        try{
            LocalDate pickupDate = request.getPickupDate();
            LocalDate returnDate = request.getReturnDate();
            String location = request.getLocation();

            //fetch all available cars for the given location
            List<Car> cars = carService.findByLocationName(location);

            //check car availability for the given date range using promise
            List<Car> availableCarsPromises = new ArrayList<>();


            for(Car car : cars){
                boolean isAvailable = bookingService.checkAvailability(car.getId(),pickupDate,returnDate);
                //change available to checked available
                car.setAvailable(isAvailable);
                availableCarsPromises.add(car);
            }

            //filter available true cars
            List<Car> availableCars =  availableCarsPromises.stream().filter(Car::getAvailable).collect(Collectors.toList());

            Map<String, Object> res = new HashMap<>();
            res.put("success",true);
            res.put("availableCars",availableCars);
            return  ResponseEntity.ok(res);
        }catch (Exception ex){
            Map<String, Object> res = new HashMap<>();
            res.put("success",false);
            res.put("message",ex.getMessage());
            return  ResponseEntity.status(500).body(res);
        }
    }

    //API to create Booking
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(HttpServletRequest request,@RequestBody Booking bookingRequest){
        try{
            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            User user = userService.findById(userId);

            Car car = bookingRequest.getCar();
            LocalDate pickupDate = bookingRequest.getPickupDate();
            LocalDate returnDate = bookingRequest.getReturnDate();

            boolean isAvailable = bookingService.checkAvailability(car.getId(),pickupDate,returnDate);

            if(!isAvailable){
                Map<String, Object> res = new HashMap<>();
                res.put("success",false);
                res.put("message","Car is not available");
                return ResponseEntity.ok(res);
            }else{
                Car carDate = carService.fetchCarById(car.getId());
                //Calculate price based on pickupDate and returnDate
                long noOfDays = ChronoUnit.DAYS.between(pickupDate,returnDate);
                BigDecimal price = carDate.getPricePerDay().multiply(BigDecimal.valueOf(noOfDays));
                Booking booking = new Booking(carDate,carDate.getOwner(),user,pickupDate,returnDate,price);

                //creat  booking to database
                Booking savedBooking = bookingService.save(booking);

                Map<String, Object> res = new HashMap<>();
                res.put("success",true);
                res.put("booking",savedBooking);
                res.put("message","booking successfully");
                return ResponseEntity.ok(res);
            }
        }catch (Exception ex){
            Map<String, Object> res = new HashMap<>();
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    //API to List User Bookings
    @GetMapping("/user")
    public ResponseEntity<?> getUserBookings(HttpServletRequest request){
        Map<String, Object> res = new HashMap<>();
        try{
            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            //find user bookings  createdAt sort: -1
            List<Booking> bookings = bookingService.findUserBookings(userId);

            res.put("success",true);
            res.put("bookings",bookings);
            return ResponseEntity.ok(res);
        }catch (Exception ex){
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    //API to get Owner Bookings
    @GetMapping("/owner")
    public ResponseEntity<?> getOwnerBookings (HttpServletRequest request){

        Map<String, Object> res = new HashMap<>();

        try{
            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            //Get User
            User user = userService.findById(userId);
            if(!user.getRole().getRoleName().toLowerCase().equals("owner")){
                res.put("success",false);
                res.put("message","Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }
            List<Booking> bookings = bookingService.findOwnerBookings(userId);
            res.put("success",true);
            res.put("bookings",bookings);
            return ResponseEntity.ok(res);
        }catch (Exception ex){
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    //API to change booking status
    @PatchMapping("/change-status")
    public ResponseEntity<?> changeBookingStatus (@RequestBody Booking bookingRequest, HttpServletRequest request){
        Map<String, Object> res = new HashMap<>();
        try{
            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            User user = userService.findById(userId);
            if(!user.getRole().getRoleName().toString().toLowerCase().equals("owner")){
                res.put("success",false);
                res.put("message","Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }

            String bookingId = bookingRequest.getId();
            BookingStatus status = bookingRequest.getStatus();

            Booking booking = bookingService.findByBookingId(bookingId);

            if(!booking.getOwner().getId().toString().equals(userId)){
                res.put("success",false);
                res.put("message","Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }
            booking.setStatus(status);
            bookingService.save(booking);
            res.put("success",true);
            res.put("message","change booking status successfully");
            res.put("booking",booking);
            return ResponseEntity.ok(res);
        }catch (Exception ex){
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }
}
