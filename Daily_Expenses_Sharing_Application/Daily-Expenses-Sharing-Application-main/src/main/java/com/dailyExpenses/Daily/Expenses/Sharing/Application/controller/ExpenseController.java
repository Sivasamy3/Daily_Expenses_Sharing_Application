package com.dailyExpenses.Daily.Expenses.Sharing.Application.controller;

import java.io.IOException;
import java.util.List;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.data.ExpenseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailyExpenses.Daily.Expenses.Sharing.Application.service.ExpenseService;


@RestController

//http://localhost:8082/api/expenses
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    //http://localhost:8082/api/expenses
    @PostMapping
    public ResponseEntity<ExpenseData> addExpense(@RequestBody ExpenseData expenseData) {
        ExpenseData createdExpense = expenseService.addExpense(expenseData);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
    }

    //http://localhost:8082/api/expenses/users/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseData>> getUserExpenses(@PathVariable Long userId) {
        List<ExpenseData> expenses = expenseService.getUserExpenses(userId);
        return ResponseEntity.ok(expenses);
    }

       //http://localhost:8082/api/expenses/overall
    @GetMapping("/overall")
    public ResponseEntity<List<ExpenseData>> getAllExpenses() {
        List<ExpenseData> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenses);
    }

    //http://localhost:8082/api/expenses/download/1 or all
    @GetMapping("/download/{userIdOrAll}")
    public ResponseEntity<Resource> downloadBalanceSheet(@PathVariable String userIdOrAll) throws IOException {
        Resource file;
        if (userIdOrAll.equalsIgnoreCase("overall")) {
            file = expenseService.downloadOverallBalanceSheet();
        } else {
            Long userId = Long.valueOf(userIdOrAll);
            file = expenseService.downloadBalanceSheet(userId);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=balance_sheet.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
