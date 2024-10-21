package com.dailyExpenses.Daily.Expenses.Sharing.Application.Exception;


public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
