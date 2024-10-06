/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expense.utils;

/**
 *
 * @author USER
 */
import java.util.Timer;
import java.util.TimerTask;

public class BudgetAlert {
    private Timer timer = new Timer();

    public BudgetAlert() {
        timer.schedule(new CheckBudgetTask(), 0, 3600000); // Check every hour
    }

    class CheckBudgetTask extends TimerTask {
        @Override
        public void run() {
            // Check budgets for all users and send alerts if necessary
            // Use BudgetDao to get budgets and compare with ExpenseDao to get expenses
        }
    }
}

