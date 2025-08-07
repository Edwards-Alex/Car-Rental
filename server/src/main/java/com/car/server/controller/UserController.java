package com.car.server.controller;

import com.car.server.model.Car;
import com.car.server.model.User;
import com.car.server.service.CarService;
import com.car.server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.car.server.utility.JwtTokenProvider;

@RestController
@RequestMapping("/api/user")
public class UserController {

    //Service
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    //User Register,GlobalEx is setting
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {

        Map<String, Object> res = new HashMap<>();

        userService.registerUser(user);

        //generate Token
        String token = jwtTokenProvider.generateToken(user.getId());


        //response Token to frontend, store in cookie logic at frontend
        res.put("success", true);
        res.put("token", token);

        return ResponseEntity.ok(res);
    }

    //Login User
    @PostMapping("/login")
    public ResponseEntity<?> loginUser (@RequestBody User loginUSer) {
        Map<String, Object> res = new HashMap<>();
        try{
            User user = userService.loginUser(loginUSer.getEmail(), loginUSer.getPassword());


            res.put("success",true);
            res.put("user", user);

            //生成token
            String token = jwtTokenProvider.generateToken(user.getId());
            res.put("token",token);

            return ResponseEntity.ok(res);
        }catch (Exception ex){
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }
    }

    @GetMapping("/data")
    public ResponseEntity<?> getUserData(HttpServletRequest request){
//        String authHeader = request.getHeader("Authorization");
//        String token = authHeader.substring(7);
        String token = request.getHeader("Authorization");
        //Utility class decompiler token to userId
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        User user  = userService.findById(userId);

        Map<String, Object> res = new HashMap<>();
        res.put("success",true);
        res.put("user",user);

        return ResponseEntity.ok(res);
    }

    //Get all cars for fronted
    @GetMapping("/cars")
    public ResponseEntity<?> getCars (){
        Map<String, Object> res = new HashMap<>();
        try{
            List<Car> cars = new ArrayList<>();
            cars = carService.findCars();
            res.put("success",true);
            res.put("cars", cars);
            return ResponseEntity.ok(res);
        }catch (Exception ex){
            res.put("success",false);
            res.put("message",ex.getMessage());
            return ResponseEntity.badRequest().body(res);
        }

    }
}
