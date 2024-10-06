/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.expensetracker.servlets;

import com.expensetracker.dao.BudgetDao;
import com.expensetracker.implementation.BudgetDaoImpl;
import com.expensetracker.models.Budget;
import com.expensetracker.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author USER
 */
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BudgetServlet extends HttpServlet {

    private BudgetDao budgetDAO = new BudgetDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("set".equals(action)) {
            setBudget(request, response);
        } else if ("edit".equals(action)) {
            editBudget(request, response);
        }
    }

    private void setBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("loginUser.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        double newAmount = Double.parseDouble(request.getParameter("amount"));
        String period = request.getParameter("period");
        String creationDateStr = request.getParameter("creationDate");

        int alertThreshold = Integer.parseInt(request.getParameter("alertThreshold"));

        // Parse dates
        Date creationDate = new Date();
        Date endDate = new Date();
        try {
            creationDate = new SimpleDateFormat("yyyy-MM-dd").parse(creationDateStr);       
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check for existing budget
        Budget existingBudget = budgetDAO.getCurrentBudget(user.getId());
        if (existingBudget != null) {
            // Add to the existing budget amount
            double updatedAmount = existingBudget.getAmount() + newAmount;
            existingBudget.setAmount(updatedAmount);
            existingBudget.setPeriod(period);
            existingBudget.setCreationDate(creationDate);
            existingBudget.setAlertThreshold(alertThreshold);

            budgetDAO.editBudget(existingBudget.getId(), existingBudget); // Update the existing budget
            System.out.println("Updated existing budget. New amount: " + updatedAmount);
        } else {
            // Create a new budget if none exists
            Budget budget = new Budget();
            budget.setUser(user);
            budget.setAmount(newAmount);
            budget.setPeriod(period);
            budget.setCreationDate(creationDate);
            budget.setAlertThreshold(alertThreshold);
            budgetDAO.setBudget(budget); // Add the new budget
            System.out.println("Created a new budget for the user.");
        }

        response.sendRedirect("manageBudget.jsp");
    }

    private void editBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String budgetIdStr = request.getParameter("budgetId");

        if (budgetIdStr != null && !budgetIdStr.isEmpty()) {
            try {
                int budgetId = Integer.parseInt(budgetIdStr);
                Budget budget = budgetDAO.getBudgetById(budgetId);
                if (budget != null) {
                    String amountStr = request.getParameter("amount");
                    String period = request.getParameter("period");
                    String creationDateStr = request.getParameter("startDate");
                    int alertThreshold = Integer.parseInt(request.getParameter("alertThreshold"));

                    double amount = Double.parseDouble(amountStr);
                    Date creationDate = new SimpleDateFormat("yyyy-MM-dd").parse(creationDateStr);
                    

                    budget.setAmount(amount);
                    budget.setPeriod(period);
                    budget.setCreationDate(creationDate);              
                    budget.setAlertThreshold(alertThreshold);

                    budgetDAO.editBudget(budgetId, budget);

                    response.sendRedirect("manageBudget.jsp?success=1");
                } else {
                    response.sendRedirect("manageBudget.jsp?error=notfound");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("manageBudget.jsp?error=exception");
            }
        } else {
            response.sendRedirect("manageBudget.jsp?error=invalid");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            deleteBudget(request, response);
        }
    }

    private void deleteBudget(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int budgetId = Integer.parseInt(request.getParameter("id"));
        budgetDAO.deleteBudget(budgetId);
        response.sendRedirect("manageBudget.jsp?success=1");
    }

}
