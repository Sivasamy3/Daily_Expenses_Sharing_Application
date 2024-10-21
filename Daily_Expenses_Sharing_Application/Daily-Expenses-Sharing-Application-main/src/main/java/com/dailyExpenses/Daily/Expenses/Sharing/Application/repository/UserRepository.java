package com.dailyExpenses.Daily.Expenses.Sharing.Application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.User;


public interface UserRepository extends JpaRepository<User, Long> {

}
