package com.dailyExpenses.Daily.Expenses.Sharing.Application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.Split;


public interface SplitRepository extends JpaRepository<Split, Long> {

    List<Split> findByExpenseId(Long expenseId);
}
