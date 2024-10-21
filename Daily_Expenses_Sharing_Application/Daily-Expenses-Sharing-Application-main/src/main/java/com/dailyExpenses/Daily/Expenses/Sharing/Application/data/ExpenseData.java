package com.dailyExpenses.Daily.Expenses.Sharing.Application.data;

import java.time.LocalDate;
import java.util.List;


public class ExpenseData {
    private Long id;
    private String description;
    private Double amount;
    private Long userId;
    private LocalDate date;
    private List<SplitData> splits;

    // Default constructor
    public ExpenseData() {
    }

    public ExpenseData(Long id, String description, Double amount, LocalDate date, Long userId, List<SplitData> splits) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.userId = userId;
        this.splits = splits;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<SplitData> getSplits() {
        return splits;
    }

    public void setSplits(List<SplitData> splits) {
        this.splits = splits;
    }
}
