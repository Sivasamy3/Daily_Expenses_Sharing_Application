package com.dailyExpenses.Daily.Expenses.Sharing.Application.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.data.ExpenseData;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.data.SplitData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.Expense;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.Split;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.SplitType;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.User;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.Exception.ResourceNotFoundException;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.repository.ExpenseRepository;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.repository.SplitRepository;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.repository.UserRepository;


@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SplitRepository splitRepository;

    public ExpenseData addExpense(ExpenseData expenseData) {
        User user = userRepository.findById(expenseData.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Expense expense = new Expense();
        expense.setDescription(expenseData.getDescription());
        expense.setAmount(expenseData.getAmount());
        expense.setDate(expenseData.getDate());
        expense.setUser(user);
        Expense savedExpense = expenseRepository.save(expense);

        List<Split> splits = new ArrayList<>();
        double totalAmount = expenseData.getAmount();
        double totalPercentage = 0;

        for (SplitData splitData : expenseData.getSplits()) {
            Split split = new Split();
            split.setExpenseId(savedExpense.getId());
            split.setUserId(splitData.getUserId());
            split.setSplitType(splitData.getSplitType());

            if (splitData.getSplitType() == SplitType.EXACT) {
                split.setAmount(splitData.getAmount());
                split.setPercentage(null); // Exact splits do not use percentage
            } else if (splitData.getSplitType() == SplitType.PERCENTAGE) {
                split.setAmount(totalAmount * (splitData.getPercentage() / 100));
                split.setPercentage(splitData.getPercentage());
                totalPercentage += splitData.getPercentage();
            } else if (splitData.getSplitType() == SplitType.EQUAL) {
                split.setAmount(totalAmount / expenseData.getSplits().size());
                split.setPercentage(null); // Equal splits do not use percentage
            }

            splits.add(split);
        }

        // Validate percentages add up to 100
        if (totalPercentage != 100 && !splits.isEmpty() && splits.get(0).getSplitType() == SplitType.PERCENTAGE) {
            throw new IllegalArgumentException("Total percentage does not add up to 100");
        }

        splitRepository.saveAll(splits);
        return convertToDTO(savedExpense, splits);
    }

    public List<ExpenseData> getUserExpenses(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream()
                .map(expense -> convertToDTO(expense, splitRepository.findByExpenseId(expense.getId())))
                .collect(Collectors.toList());
    }

    public List<ExpenseData> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                .map(expense -> convertToDTO(expense, splitRepository.findByExpenseId(expense.getId())))
                .collect(Collectors.toList());
    }

    private ExpenseData convertToDTO(Expense expense, List<Split> splits) {
        ExpenseData dto = new ExpenseData();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setUserId(expense.getUser().getId());

        List<SplitData> splitData = splits.stream()
                .map(split -> {
                    SplitData splitDTO = new SplitData();
                    splitDTO.setUserId(split.getUserId());
                    splitDTO.setAmount(split.getAmount());
                    splitDTO.setSplitType(split.getSplitType());
                    splitDTO.setPercentage(split.getPercentage()); // Ensure percentage is set
                    return splitDTO;
                }).collect(Collectors.toList());
        dto.setSplits(splitData);

        return dto;
    }

    public Resource downloadBalanceSheet(Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Expense> expenses = expenseRepository.findByUserId(userId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(
                new PrintWriter(out, true, StandardCharsets.UTF_8),
                CSVFormat.DEFAULT.withHeader("Description", "Amount", "Date", "User Id", "Split Amount", "Split Type", "Percentage"));

        for (Expense expense : expenses) {
            List<Split> splits = splitRepository.findByExpenseId(expense.getId());
            for (Split split : splits) {
                csvPrinter.printRecord(
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getDate(),
                        expense.getUser().getId(),
                        split.getAmount(),
                        split.getSplitType(),
                        split.getPercentage()
                );
            }
        }

        csvPrinter.flush();
        byte[] data = out.toByteArray();
        return new ByteArrayResource(data);
    }


    public Resource downloadOverallBalanceSheet() throws IOException {
        List<Expense> expenses = expenseRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(
                new PrintWriter(out, true, StandardCharsets.UTF_8),
                CSVFormat.DEFAULT.withHeader("Description", "Amount", "Date", "User Id", "Split Amount", "Split Type", "Percentage"));

        for (Expense expense : expenses) {
            List<Split> splits = splitRepository.findByExpenseId(expense.getId());
            for (Split split : splits) {
                csvPrinter.printRecord(
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getDate(),
                        expense.getUser().getId(),
                        split.getAmount(),
                        split.getSplitType(),
                        split.getPercentage()
                );
            }
        }

        csvPrinter.flush();
        byte[] data = out.toByteArray();
        return new ByteArrayResource(data);
    }
}
