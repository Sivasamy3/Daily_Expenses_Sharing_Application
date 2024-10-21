package com.dailyExpenses.Daily.Expenses.Sharing.Application.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.Entities.SplitType;
import com.dailyExpenses.Daily.Expenses.Sharing.Application.data.SplitData;


public class SplitCalculator {


    public static List<SplitData> calculateEqualSplits(Double totalAmount, List<Long> userIds) {
        Double splitAmount = totalAmount / userIds.size();
        return userIds.stream()
                .map(userId -> new SplitData(userId, splitAmount, SplitType.EQUAL, null))
                .collect(Collectors.toList());
    }

    public static List<SplitData> calculateExactSplits(List<Double> amounts, List<Long> userIds) {
        if (amounts.size() != userIds.size()) {
            throw new IllegalArgumentException("The number of amounts must match the number of user IDs.");
        }

        List<SplitData> splits = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            splits.add(new SplitData(userIds.get(i), amounts.get(i), SplitType.EXACT, null));
        }
        return splits;
    }

    public static List<SplitData> calculatePercentageSplits(Double totalAmount, List<Double> percentages, List<Long> userIds) {
        double totalPercentage = percentages.stream().mapToDouble(Double::doubleValue).sum();
        if (totalPercentage != 100) {
            throw new IllegalArgumentException("Total percentages must equal 100.");
        }

        List<SplitData> splits = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            Double splitAmount = totalAmount * (percentages.get(i) / 100);
            splits.add(new SplitData(userIds.get(i), splitAmount, SplitType.PERCENTAGE, percentages.get(i)));
        }
        return splits;
    }
}
