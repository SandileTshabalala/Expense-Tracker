<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %> 
<%@ page import="com.expensetracker.models.User" %> 
<%@ page import="com.expensetracker.models.Expense" %> 
<%@ page import="com.expensetracker.implementation.ExpenseDaoImpl" %> 
<%@ page import="java.util.Date" %>

<%
    User user = (User) session.getAttribute("user");
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


    </body>
</html>
