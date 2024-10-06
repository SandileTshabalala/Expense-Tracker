<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.expensetracker.models.Expense" %> 
<%@ page import="com.expensetracker.models.Budget" %> 
<%@ page import="com.expensetracker.dao.ExpenseDao" %>
<%@ page import="com.expensetracker.dao.BudgetDao" %>
<%@ page import="com.expensetracker.implementation.ExpenseDaoImpl" %>
<%@ page import="com.expensetracker.implementation.BudgetDaoImpl" %>
<%@ page import="com.expensetracker.models.User" %> 
<%@ page import="java.text.SimpleDateFormat" %>
<%
        User user = (User) session.getAttribute("user");
%>
<html>
    <head>
        <title>Add Expense </title>
    </head>
    <body>
        <%
            BudgetDao budgetDao = new BudgetDaoImpl();
            Budget budget = budgetDao.getCurrentBudget(user.getId());
        %>

        <h1>Current Balance: <%= budget != null ? budget.getAmount() : "No budget set" %> </h1>

        <h1>Add Expense</h1>
        <form action="ExpenseServlet" method="post">
            <input type="hidden" name="action" value="add" />

            <label for="amount">Amount:</label>
            <input type="number" name="amount" required/><br/>

            <label for="description">Description:</label>
            <input type="text" name="description" /><br/>

            <label for="category">Category:</label>
            <select name="category" required>
                <option value="">Select Category</option>
                <option value="food">Food</option>
                <option value="transport">Transport</option>
                <option value="utilities">Utilities</option>
                <option value="health">Health</option>
                <option value="entertainment">Entertainment</option>
                <option value="other">Other</option>
            </select><br/>

            <label for="date">Date:</label>
            <input type="date" name="date"/><br/>
            <input type="submit" value="Add Expense" />
        </form>
        <%          
            ExpenseDao expenseDao = new ExpenseDaoImpl();
            List<Expense> expenses = expenseDao.getExpensesByUser(user.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        %>
        <h2>Current Expenses</h2>
        <table>
            <thead>
                <tr>
                    <th>Category</th>
                    <th>Description</th>
                    <th>Amount</th>
                    <th>Date</th>
                    <th>Category</th>
                    <th>Currency</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (expenses != null) {
                        for (Expense expense : expenses) {
                %>
                <tr>
                    <td><%= expense.getCategory() %></td>
                    <td><%= expense.getDescription() %></td>
                    <td><%= expense.getAmount() %></td>                  
                    <td><%= sdf.format(expense.getDate()) %></td> 
                    <td><%= expense.getCategory() %></td>
                    <td><%= user.getCurrency() %></td>
                    <td class="action-links">
                        <a href="editExpense.jsp?id=<%= expense.getId() %>">Edit</a>
                        <a href="ExpenseServlet?action=delete&id=<%= expense.getId() %>" onclick="return confirm('Are you sure you want to delete this expense?');">Delete</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <a href="dashboard.jsp">Back to Dashboard</a>
    </body>
</html>

