package com.example.Kutumb.service;

import com.example.Kutumb.entity.UserRegistration;
import com.example.Kutumb.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo ur;

    private static final Map<String, String> locationMap = Map.ofEntries(
            Map.entry("16.3067,80.4365", "Guntur"),
            Map.entry("12.9716,77.5946", "Bangalore"),
            Map.entry("19.0760,72.8777", "Mumbai"),
            Map.entry("17.3850,78.4867", "Hyderabad"),
            Map.entry("13.0827,80.2707", "Chennai"),
            Map.entry("18.5204,73.8567", "Pune"),
            Map.entry("28.6139,77.2090", "Delhi"),
            Map.entry("27.1767,78.0081", "Agra"),
            Map.entry("22.5726,88.3639", "Kolkata"),
            Map.entry("23.0225,72.5714", "Ahmedabad"),
            Map.entry("26.9124,75.7873", "Jaipur"),
            Map.entry("21.1458,79.0882", "Nagpur"),
            Map.entry("11.0168,76.9558", "Coimbatore"),
            Map.entry("9.9312,76.2673", "Thiruvananthapuram")
    );

    public String add(UserRegistration u){
        if (ur.findByPhoneNumber(u.getPhoneNumber()).isPresent()) {
            return "Already exists";
        }
        ur.save(u);
        return "Registered Successfully";
    }

    public Optional<UserRegistration> get(String phoneNumber){
        return ur.findByPhoneNumber(phoneNumber);
    }

    public Iterable<UserRegistration> getAll(){
        return ur.findAll();
    }

    public String updateLocation(String phoneNumber, Double latitude, Double longitude){
        Optional<UserRegistration> userOpt = ur.findByPhoneNumber(phoneNumber);
        if(userOpt.isPresent()){
            UserRegistration u = userOpt.get();
            u.setLatitude(latitude);
            u.setLongitude(longitude);
            ur.save(u);
            return "Location updated";
        }
        return "User not found";
    }

    public Iterable<UserRegistration> getAllWithArea(){
        Iterable<UserRegistration> users = ur.findAll();
        for (UserRegistration u : users) {
            if(u.getLatitude() != null && u.getLongitude() != null){
                String key = String.format("%.4f,%.4f", u.getLatitude(), u.getLongitude());
                String area = locationMap.getOrDefault(key, "Unknown");
                u.setArea(area);
            } else {
                u.setArea("Unknown");
            }
        }
        return users;
    }


    public Iterable<UserRegistration> getNearbyFriends(Double latitude, Double longitude, double radiusKm) {
        Iterable<UserRegistration> allUsers = ur.findAll();
        List<UserRegistration> nearby = new ArrayList<>();

        for (UserRegistration u : allUsers) {
            if(u.getLatitude() != null && u.getLongitude() != null){
                double distance = distanceInKm(latitude, longitude, u.getLatitude(), u.getLongitude());
                if(distance <= radiusKm){
                    nearby.add(u);
                    // Optionally set area if you want
                    String key = String.format("%.4f,%.4f", u.getLatitude(), u.getLongitude());
                    u.setArea(locationMap.getOrDefault(key, "Unknown"));
                }
            }
        }
        return nearby;
    }

    // Haversine formula to calculate distance between two lat/lon points
    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }

}
