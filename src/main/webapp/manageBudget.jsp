<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.expensetracker.models.Budget" %>
<%@ page import="com.expensetracker.models.User" %>
<%@ page import="com.expensetracker.dao.BudgetDao" %>
<%@ page import="com.expensetracker.implementation.BudgetDaoImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <title>Manage Budget</title>
        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background-color: #f4f4f9;
                margin: 0;
                padding: 20px;
            }

            h1, h2 {
                color: #333;
            }

            form {
                background-color: #fff;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 30px;
            }

            label {
                display: block;
                margin-bottom: 5px;
                color: #555;
            }

            input[type="number"],
            input[type="date"],
            select {
                width: 100%;
                padding: 8px;
                margin-bottom: 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }

            input[type="submit"] {
                background-color: #4CAF50;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            input[type="submit"]:hover {
                background-color: #45a049;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background-color: #fff;
                border-radius: 5px;
                overflow: hidden;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }

            th, td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            th {
                background-color: #4CAF50;
                color: white;
            }

            tr:hover {
                background-color: #f1f1f1;
            }

            .action-links a {
                margin-right: 10px;
                color: #4CAF50;
                text-decoration: none;
            }

            .action-links a:hover {
                text-decoration: underline;
            }

            .alert {
                color: red;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <h1>Manage Budget</h1>

        <%
    //        HttpSession session = request.getSession(false);
    //        if (session == null || session.getAttribute("user") == null) {
    //            response.sendRedirect("login.jsp");
    //            return;
    //        }
            User user = (User) session.getAttribute("user");
            BudgetDao budgetDao = new BudgetDaoImpl();
            List<Budget> budgets = budgetDao.getBudgetByUser(user.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        %>

        <h2>Add New Budget</h2>
        <form action="BudgetServlet" method="post">
            <input type="hidden" name="action" value="set" />

            <label for="amount">Budget Amount:</label>
            <input type="number" name="amount" step="0.01" required/>

            <label for="period">Period:</label>
            <select name="period" required>
                <option value="Daily">Daily</option>
                <option value="Weekly">Weekly</option>
                <option value="Monthly">Monthly</option>
            </select>

            <label for="creationDate">Create Date:</label>
            <input type="date" name="creationDate" required/>

            <label for="alertThreshold">Alert Threshold (%):</label>
            <input type="number" name="alertThreshold" value="80" min="1" max="100" required/>

            <input type="submit" value="Set Budget" />
        </form>

        <h2>Current Budgets</h2>
        <table>
            <thead>
                <tr>

                    <th>Amount</th>
                    <th>Period</th>
                    <th>Alert Threshold (%)</th>
                    <th>Date Created</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Budget budget : budgets) {
                %>
                <tr>

                    <td><%= budget.getAmount() %></td>
                    <td><%= budget.getPeriod() %></td>
                    <td><%= budget.getAlertThreshold() %></td>
                    <td><%= sdf.format(budget.getCreationDate()) %></td>
                    <td class="action-links">
                        <a href="editBudget.jsp?id=<%= budget.getId() %>">Edit</a>
                        <a href="BudgetServlet?action=delete&id=<%= budget.getId() %>" onclick="return confirm('Are you sure you want to delete this budget?');">Delete</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <a href="dashboard.jsp">Back to Dashboard</a>
    </body>
</html>
