/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expensetracker.dao;

/**
 *
 * @author USER
 */
import com.expensetracker.models.Expense;
import java.util.Date;

import java.util.List;
import java.util.Map;

public interface ExpenseDao {
    void addExpense(Expense expense);
    Expense getExpenseById(int id);
    List<Expense> getExpensesByUser(int userId);
    void deleteExpense(int id);
    void editExpense(int id, Expense expense);
    double getTotalExpensesForUserAndPeriod(int userId, String period);
    double getTotalExpensesForUser(int userId);
    List<Expense> getExpenses(int userId, Date startDate, Date endDate);
    Map<String, Double> generateExpenseReport(int userId, Date startDate, Date endDate);
    List<Expense> getUserExpenses(int userId);
}
