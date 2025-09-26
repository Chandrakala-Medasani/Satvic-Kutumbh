package com.example.Kutumb.repo;

import com.example.Kutumb.entity.UserRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<UserRegistration, Long> {

    public Optional<UserRegistration> findByPhoneNumber(String phoneNumber);
}
