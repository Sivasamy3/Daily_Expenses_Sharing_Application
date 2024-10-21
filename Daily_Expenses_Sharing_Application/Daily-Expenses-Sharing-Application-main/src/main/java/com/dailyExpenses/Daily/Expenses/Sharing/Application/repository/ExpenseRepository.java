package com.dailyExpenses.Daily.Expenses.Sharing.Application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.Expense;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);
}
