/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expensetracker.dao;

import com.expensetracker.models.Budget;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface BudgetDao {
    void setBudget(Budget budget);
    Budget getCurrentBudget(int userId);
     void updateBudget(Budget budget);
    List<Budget> getBudgetByUser(int userId);
    Budget getBudgetById(int id);
    void deleteBudget(int id);
    void editBudget(int id, Budget budget);
    List<Budget> getBudgetsForUser(int userId);
    void deductFromBudget(int userId, double amount);
    void addToBudget(int userId, double amount);
}

