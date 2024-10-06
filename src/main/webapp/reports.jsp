<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %> 
<%@ page import="com.expensetracker.models.User" %> 
<%@ page import="com.expensetracker.models.Expense" %> 
<%@ page import="com.expensetracker.implementation.ExpenseDaoImpl" %> 
<%@ page import="java.util.Date" %>

<%
    // Assume the user ID is stored in the session
    User user = (User) session.getAttribute("user");
    List<Expense> expenseData = new ArrayList<>();

    if (user != null) {
        ExpenseDaoImpl expenseDao = new ExpenseDaoImpl();
        // Fetch user expenses; replace with the actual user ID
        expenseData = expenseDao.getExpensesByUser(user.getId());
    }

    ObjectMapper objectMapper = new ObjectMapper();
    String expenseDataJson = objectMapper.writeValueAsString(expenseData);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Expense Tracker Dashboard</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            body {
                margin: 0;
                padding: 0;
                font-family: 'Poppins', sans-serif;
                background-color: #f4f4f9;
            }

            header {
                background-color: #4CAF50;
                padding: 15px 0;
                text-align: center;
                color: white;
            }

            header h1 {
                margin: 0;
                font-size: 2.5em;
            }

            nav {
                display: flex;
                justify-content: center;
                padding: 10px 0;
                background-color: #333;
            }

            nav a {
                color: white;
                margin: 0 15px;
                text-decoration: none;
                font-weight: 600;
            }

            nav a:hover {
                color: #4CAF50;
            }
        </style>    
    </head>
    <body>
        <header>
            <h1>Expense Tracker</h1>
        </header>

        <nav>
            <a href="dashboard.jsp">Dashboard</a>
            <a href="addExpense.jsp">Add Expenses</a>
            <a href="manageBudget.jsp">Manage Budget</a>
            <a href="reports.jsp">Reports</a>
            <a href="logout.jsp" class="btn-logout">Log Out</a>
        </nav>

        <h1>Welcome, <%= user != null ? user.getUsername() : "Guest" %>!</h1>

        <h2>Expense Chart</h2>
        <canvas id="expenseChart" width="400" height="200"></canvas>

        <script>
            var expenseData = <%= expenseDataJson %>;

            var labels = expenseData.map(function(e) { return e.category; });
            var data = expenseData.map(function(e) { return e.amount; });

            const ctx = document.getElementById('expenseChart').getContext('2d');
            const expenseChart = new Chart(ctx, {
                type: 'bar', // or 'pie', 'line', etc.
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Expenses',
                        data: data,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        </script>

        <h2>Additional Dashboard Content</h2>
        <!-- Add any additional content here -->

    </body>
</html>
