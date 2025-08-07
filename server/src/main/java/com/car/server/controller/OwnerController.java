package com.car.server.controller;

import com.car.server.model.Booking;
import com.car.server.model.Car;
import com.car.server.model.User;
import com.car.server.model.enums.BookingStatus;
import com.car.server.service.BookingService;
import com.car.server.service.CarService;
import com.car.server.service.OwnerService;
import com.car.server.service.UserService;
import com.car.server.utility.JwtTokenProvider;
import com.car.server.utility.image.OSSImageUploader;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {
    //Service
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private UserService userService;

    @Autowired
    private OSSImageUploader ossImageUploader;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //API to change role
    @PostMapping("/change-role")
    public ResponseEntity<?> changeRoleToOwner(HttpServletRequest request){

//        String authHeader = request.getHeader("Authorization");
//        String token = authHeader.substring(7);

        String token = request.getHeader("Authorization");

        //Utility class decompiler token to userId
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        boolean flag = ownerService.updateRoleToOwner(userId);
        //generate response
        Map<String, Object> res = new HashMap<>();
        res.put("success",flag);
        if(flag){
            res.put("message","Now you can list cars!");
        }else{
            res.put("message","Change Role failed!");
        }


        return ResponseEntity.ok(res);
    }

    //API to update user image
    @PostMapping("/update-image")
    public ResponseEntity<?> updateUserImage(@RequestPart("imageFile") MultipartFile imageFile,HttpServletRequest request){
        Map<String, Object> res = new HashMap<>();
        if(imageFile.isEmpty()){
            res.put("success",false);
            res.put("message","imageFile is empty");
            return ResponseEntity.ok(res);
        }
        try{

            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectKey = UUID.randomUUID() + extension;

            // 3. 上传图片到阿里云OSS存储 并同时压缩和格式化为.web文件（直接使用MultipartFile的InputStream）
            String imagePath =  ossImageUploader.uploadAndProcess(imageFile.getInputStream(), objectKey,"user");

            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            User user  = userService.findById(userId);

            user.setImage(imagePath);

            userService.save(user);

            res.put("success",true);
            res.put("message","Image Updated");
            return  ResponseEntity.ok(res);
        }catch (Exception ex){
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    //API to Add Car
    @PostMapping(value = "/add-car")
    public ResponseEntity<?> addCar(@RequestPart("carData") String carJson,@RequestPart("imageFile") MultipartFile imageFile,HttpServletRequest request){
       try{
           //将token 转化为 userId
//           String authHeader = request.getHeader("Authorization");
//           String token = authHeader.substring(7);
           String token = request.getHeader("Authorization");
           String userId = jwtTokenProvider.getUserIdFromToken(token);

           User user  = userService.findById(userId);

           // 当 user 不为 null 且 role 是 "owner" 时执行
           if (user != null && "owner".equals(user.getRole().getRoleName())) {
               //解析 Car Json 对象
               ObjectMapper objectMapper = new ObjectMapper();
               Car car = objectMapper.readValue(carJson,Car.class);

               // 2. 生成唯一的objectKey（UUID + 文件扩展名）
               String originalFilename = imageFile.getOriginalFilename();
               String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
               String objectKey = UUID.randomUUID() + extension;

               // 3. 上传图片到阿里云OSS存储 并同时压缩和格式化为.web文件（直接使用MultipartFile的InputStream）
               String imagePath =  ossImageUploader.uploadAndProcess(imageFile.getInputStream(), objectKey,"car");

               //完善car的image属性，阿里云OSS里存储的图片URL路径，实现实时网络访问
               car.setImage(imagePath);
               car.setOwner(user);
               //4.保存car数据到数据库
               carService.addCar(car);

               Map<String, Object> res = new HashMap<>();
               res.put("success",true);
               res.put("car",car);
               res.put("message","Car added successfully");

               return ResponseEntity.ok(res);
           }else{
               Map<String,Object> res = new HashMap<>();
               res.put("success",false);
               res.put("message","please login first");
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
           }
       }catch (Exception e){
           Map<String,Object> res = new HashMap<>();
           res.put("success",false);
           res.put("message",e.getMessage());
           return ResponseEntity.status(500).body(res);
       }
    }


    //API to List Owner Cars
    @GetMapping("/cars")
    public ResponseEntity<?> getOwnerCars(HttpServletRequest request){
        try{
            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            User user  = userService.findById(userId);

            if(user != null && "owner".equals(user.getRole().getRoleName())){
               List<Car> ownerCars =  carService.fetchCarsByOwner(userId);
               Map<String, Object> res = new HashMap<>();
               res.put("success",true);
               res.put("cars",ownerCars);
               return ResponseEntity.ok(res);
            }else{
                Map<String,Object> res = new HashMap<>();
                res.put("success",false);
                res.put("message","Please login first,and check role is owner");
                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }

        }catch (Exception ex){
            Map<String,Object> res = new HashMap<>();
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    //API to toggle Car Availability
    @PatchMapping("/toggle-car/{carId}")

    public ResponseEntity<?> toggleCarAvailability (@PathVariable String carId, HttpServletRequest request){
        try{
            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            //find this Car by carId
            Car car  = carService.fetchCarById(carId);

            String ownerId = car.getOwner().getId();

            //Checking is car belongs to the user
            if(ownerId != null && ownerId.equals(userId)){
                //toggle available
                car.setAvailable(!car.getAvailable());
                //save to database
                carService.addCar(car);
                Map<String,Object> res = new HashMap<>();
                res.put("success",true);
                res.put("message","Availability Toggle");
                res.put("car",car);
                return ResponseEntity.ok(res);
            }else{
                Map<String, Object> res = new HashMap<>();
                res.put("success",false);
                res.put("message","Unauthorized");
                return  ResponseEntity.ok(res);
            }

        }catch (Exception ex){
            Map<String,Object> res = new HashMap<>();
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    //API to delete a car
    @PatchMapping("/delete-car/{carId}")
    public ResponseEntity<?> deleteCar(@PathVariable String  carId, HttpServletRequest request){
        try{
            //将token 转化为 userId
//            String authHeader = request.getHeader("Authorization");
//            String token = authHeader.substring(7);
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            //find this Car by carId
            Car car  = carService.fetchCarById(carId);

            //Checking is car belongs to the user
            String ownerId = car.getOwner().getId();

            if(ownerId != null && ownerId.equals(userId)){
                car.setOwner(null);
                car.setAvailable(false);
                carService.addCar(car);
                Map<String,Object> res = new HashMap<>();
                res.put("success",true);
                res.put("message","Car Removed");
                return ResponseEntity.ok(res);
            }else{
                Map<String, Object> res = new HashMap<>();
                res.put("success",false);
                res.put("message","Unauthorized");
                return  ResponseEntity.ok(res);
            }
        }catch (Exception ex){
            Map<String,Object> res = new HashMap<>();
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    //API to get DashBoard Data
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(HttpServletRequest request){
        try{
            String token = request.getHeader("Authorization");
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            User user = userService.findById(userId);
            String role = user.getRole().getRoleName();

            if(role == null ||  !role.toLowerCase().equals("owner")){
                Map<String, Object> res =new HashMap<>();
                res.put("success",false);
                res.put("message","Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
            }

            List<Car> cars = carService.fetchCarsByOwner(userId);


            //Booking info
            List<Booking> bookings = bookingService.findOwnerBookings(user.getId());

            List<Booking> pendingBookings = bookingService.findByOwnerIdAndStatus(user.getId(), BookingStatus.PENDING);

            List<Booking> completedBookings = bookingService.findByOwnerIdAndStatus(user.getId(), BookingStatus.CONFIRMED);

            //Calculate monthlyRevenue from bookings where status is confirmed
            BigDecimal monthlyRevenue = bookings.stream().filter(booking -> BookingStatus.CONFIRMED.equals(booking.getStatus()))
                    .map(Booking::getPrice)
                    .filter(Objects::nonNull)  // 过滤掉 price 为 null 的订单
                    .reduce(BigDecimal.ZERO, (sum, price) -> sum.add(price));  // 显式累加


            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("totalCars",cars.size());
            dashboardData.put("totalBookings", bookings.size());
            dashboardData.put("pendingBookings",pendingBookings.size());
            dashboardData.put("completedBookings",completedBookings.size());
            dashboardData.put("recentBookings",bookings.subList(0,Math.min(bookings.size(),3)));
            dashboardData.put("monthlyRevenue",monthlyRevenue);

            Map<String,Object> res = new HashMap<>();
            res.put("success",true);
            res.put("dashboardData",dashboardData);

            return ResponseEntity.ok(res);
        }catch (Exception ex){
            Map<String,Object> res = new HashMap<>();
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }
}
