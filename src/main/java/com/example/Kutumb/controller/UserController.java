package com.example.Kutumb.controller;

import com.example.Kutumb.entity.UserRegistration;
import com.example.Kutumb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService us;

    @PostMapping("/user/add")
    public String add(@Valid @RequestBody UserRegistration u){
        return us.add(u);
    }

    @GetMapping("/user/get/{phoneNumber}")
    public Optional<UserRegistration> get(@PathVariable String phoneNumber){
        return us.get(phoneNumber);
    }

    @GetMapping("/user/getAll")
    public Iterable<UserRegistration> getAll(){
        return us.getAll();
    }

    @GetMapping("/user/getAllWithArea")
    public Iterable<UserRegistration> getAllWithArea(){
        return us.getAllWithArea();
    }

    // DTO class
    public static class LocationUpdateRequest {
        public String phoneNumber;
        public Double latitude;
        public Double longitude;
    }

    // Controller method
    @PutMapping("/user/updateLocation")
    public String updateLocation(@RequestBody LocationUpdateRequest request){
        return us.updateLocation(request.phoneNumber, request.latitude, request.longitude);
    }

    @GetMapping("/user/nearby")
    public Iterable<UserRegistration> getNearbyFriends(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5") double radiusKm) {
        return us.getNearbyFriends(latitude, longitude, radiusKm);
    }

}
