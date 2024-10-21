package com.dailyExpenses.Daily.Expenses.Sharing.Application.service;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.data.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.User;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.Exception.ResourceNotFoundException;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserData createUser(UserData userData) {
        User user = new User();
        user.setEmail(userData.getEmail());
        user.setName(userData.getName());
        user.setMobile(userData.getMobile());
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserData getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDTO(user);
    }

    private UserData convertToDTO(User user) {
        UserData dto = new UserData();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setMobile(user.getMobile());
        return dto;
    }
}