/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expense.utils;

import com.expensetracker.dao.BudgetDao;
import com.expensetracker.dao.ExpenseDao;
import com.expensetracker.dao.UserDao;
import com.expensetracker.models.Budget;
import com.expensetracker.models.User;
import com.expensetracker.service.EmailService;
import java.util.List;

/**
 *
 * @author USER
 */
public class BudgetChecker implements Runnable {

    private final UserDao userDao;
    private final BudgetDao budgetDao;
    private final ExpenseDao expenseDao;
    private final EmailService emailService;

    public BudgetChecker(UserDao userDao, BudgetDao budgetDao, ExpenseDao expenseDao, EmailService emailService) {
        this.userDao = userDao;
        this.budgetDao = budgetDao;
        this.expenseDao = expenseDao;
        this.emailService = emailService;
    }

    @Override
    public void run() {
        List<User> users = userDao.getAllUsers(); 
        for (User user : users) {
            List<Budget> budgets = budgetDao.getBudgetsForUser(user.getId());
            for (Budget budget : budgets) {
                double totalExpenses = expenseDao.getTotalExpensesForUserAndPeriod(user.getId(), budget.getPeriod());
                double thresholdAmount = budget.getAmount() * (budget.getAlertThreshold() / 100.0);

                if (totalExpenses >= thresholdAmount) {
                    sendNotification(user, budget); 
                }
            }
        }
    }

    private void sendNotification(User user, Budget budget) {
        String subject = "Budget Alert!";
        String message = "You have exceeded " + budget.getAlertThreshold() + "% of your budget for " + budget.getPeriod() + "!";

        // Email sending method
        EmailService.sendEmail(user.getEmail(), subject, message);
    }
}
