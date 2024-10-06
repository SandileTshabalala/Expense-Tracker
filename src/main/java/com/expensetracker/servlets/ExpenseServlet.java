/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.expensetracker.servlets;

import com.expensetracker.dao.BudgetDao;
import com.expensetracker.dao.ExpenseDao;
import com.expensetracker.implementation.BudgetDaoImpl;
import com.expensetracker.implementation.ExpenseDaoImpl;
import com.expensetracker.models.Budget;
import com.expensetracker.models.Expense;
import com.expensetracker.models.User;
import com.expensetracker.service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseServlet extends HttpServlet {

    private ExpenseDao expenseDAO = new ExpenseDaoImpl();
    private BudgetDao budgetDao = new BudgetDaoImpl();
    EmailService emailService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addExpense(request, response);
        } else if ("edit".equals(action)) {
            editExpense(request, response);
        }
    }

       private void addExpense(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        double amount = Double.parseDouble(request.getParameter("amount"));
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String dateStr = request.getParameter("date");

        Date date = new Date();
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            } catch (Exception e) {
                date = new Date();
            }
        }

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setCategory(category);
        expense.setDate(date);

        expenseDAO.addExpense(expense);

        // Retrieve the current budget for the user
        Budget budget = budgetDao.getCurrentBudget(user.getId());
        if (budget != null) {
            double totalExpenses = expenseDAO.getTotalExpensesForUser(user.getId());
            double remainingBudget = budget.getAmount() - totalExpenses;
            double alertThresholdAmount = budget.getAmount() * (budget.getAlertThreshold() / 100.0);

            // Check if a new budget is being added
            String newBudgetAmountStr = request.getParameter("newBudgetAmount");
            if (newBudgetAmountStr != null && !newBudgetAmountStr.isEmpty()) {
                double newBudgetAmount = Double.parseDouble(newBudgetAmountStr);
                // Add the new budget amount to the current budget
                double updatedBudgetAmount = budget.getAmount() + newBudgetAmount;
                budget.setAmount(updatedBudgetAmount);
                System.out.println("Budget after addition: " + updatedBudgetAmount);              
                budgetDao.updateBudget(budget);
            }

            double newBudgetAmount = budget.getAmount() - amount;
            System.out.println("Original budget: " + budget.getAmount());
            System.out.println("New budget after expense: " + newBudgetAmount);

            budget.setAmount(newBudgetAmount);
            budgetDao.updateBudget(budget);
            if (newBudgetAmount < 0) {
                emailService.sendEmail(user.getEmail(), "Budget Alert!", "You have exceeded your budget.");
                response.getWriter().println("<script>alert('Your balance is negative! Would you like to set another budget?');</script>");
            } else if (totalExpenses >= alertThresholdAmount) {
                emailService.sendEmail(user.getEmail(), "Budget Alert!", "You have exceeded " + budget.getAlertThreshold() + "% of your budget.");
                response.getWriter().println("<script>alert('You have exceeded " + budget.getAlertThreshold() + "% of your budget!');</script>");
            }
        }

        response.sendRedirect("addExpense.jsp");
    }

    private void editExpense(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String expenseIdStr = request.getParameter("id");

        if (expenseIdStr != null && !expenseIdStr.isEmpty()) {
            try {
                int expenseId = Integer.parseInt(expenseIdStr);
                Expense expense = expenseDAO.getExpenseById(expenseId);
                if (expense != null) {
                    String amountStr = request.getParameter("amount");
                    String category = request.getParameter("category");
                    String dateStr = request.getParameter("date");

                    double amount = Double.parseDouble(amountStr);
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

                    expense.setAmount(amount);
                    expense.setCategory(category);
                    expense.setDate(date);

                    expenseDAO.editExpense(expenseId, expense);
                    response.sendRedirect("addExpense.jsp?success=1");
                } else {
                    response.sendRedirect("addExpense.jsp?error=notfound");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("addExpense.jsp?error=exception");
            }
        } else {
            response.sendRedirect("addExpense.jsp?error=invalid");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            deleteExpense(request, response);
        }
    }

    private void deleteExpense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int expenseId = Integer.parseInt(request.getParameter("id"));
        expenseDAO.deleteExpense(expenseId);
        response.sendRedirect("addExpense.jsp?success=1");
    }
}
