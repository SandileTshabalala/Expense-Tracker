/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.expensetracker.servlets;


import com.expensetracker.dao.UserDao;
import com.expensetracker.implementation.UserDaoImpl;
import com.expensetracker.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class UserServlet extends HttpServlet {

    private UserDao userDAO = new UserDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("registerUser".equals(action)) {
            registerUser(request, response);
        } else if ("loginUser".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String currency = request.getParameter("currency");

        // Create the user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Password hashing is handled in UserDaoImpl
        user.setEmail(email);
        user.setCurrency(currency != null ? currency : "ZAR");

        try {
            // Register the user in the database
            userDAO.registerUser(user);
            response.sendRedirect("loginUser.jsp"); // Redirect to login after successful registration
        } catch (Exception e) {
            // Handle registration errors (e.g., email already registered)
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("registerUser.jsp").forward(request, response);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Authenticate the user with email and password
            User user = userDAO.AuthUser(email, password);
            if (user != null) {
                // Store the user in session if login is successful
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("dashboard.jsp"); // Redirect to the dashboard
            }
        } catch (Exception e) {
            // Handle login errors (e.g., invalid credentials)
            request.setAttribute("error", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("loginUser.jsp").forward(request, response);
        }
    }
}
