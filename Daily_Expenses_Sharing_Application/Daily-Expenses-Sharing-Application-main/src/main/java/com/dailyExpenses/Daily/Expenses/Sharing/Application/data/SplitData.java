package com.dailyExpenses.Daily.Expenses.Sharing.Application.data;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.SplitType;


public class SplitData {
    private Long userId;
    private Double amount;
    private SplitType splitType;
    private Double percentage; // Include percentage field

    // Default constructor
    public SplitData() {
    }

    public SplitData(Long userId, Double amount, SplitType splitType, Double percentage) {
        this.userId = userId;
        this.amount = amount;
        this.splitType = splitType;
        this.percentage = percentage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
