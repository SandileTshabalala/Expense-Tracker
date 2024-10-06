<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.expensetracker.models.Expense" %>
<%@ page import="com.expensetracker.models.User" %>
<%@ page import="com.expensetracker.dao.ExpenseDao" %>
<%@ page import="com.expensetracker.implementation.ExpenseDaoImpl" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Retrieve the expense ID from the request
    String expenseIdStr = request.getParameter("id");
    ExpenseDao expenseDao = new ExpenseDaoImpl();
    Expense expense = null;

    if (expenseIdStr != null && !expenseIdStr.isEmpty()) {
        try {
            int expenseId = Integer.parseInt(expenseIdStr);
            expense = expenseDao.getExpenseById(expenseId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
       if (expense == null) {
        out.println("<h2>Invalid expense ID or budget not found</h2>");
        return;
    }

    
    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate());
%>
<html>
<head>
    <title>Edit Expense</title>
</head>
<body>
    <h1>Edit Expense</h1>
    <form action="ExpenseServlet" method="post">
        <input type="hidden" name="action" value="edit" />
        <input type="hidden" name="id" value="<%= expense.getId() %>" />

        <label for="amount">Amount:</label>
        <input type="number" name="amount" value="<%= expense.getAmount() %>" required /><br/>

        <label for="description">Description:</label>
        <input type="text" name="description" value="<%= expense.getDescription() %>" required /><br/>

        <label for="category">Category:</label>
        <select name="category" required>
            <option value="">Select Category</option>
            <option value="food" <%= "food".equals(expense.getCategory()) ? "selected" : "" %>>Food</option>
            <option value="transport" <%= "transport".equals(expense.getCategory()) ? "selected" : "" %>>Transport</option>
            <option value="utilities" <%= "utilities".equals(expense.getCategory()) ? "selected" : "" %>>Utilities</option>
            <option value="health" <%= "health".equals(expense.getCategory()) ? "selected" : "" %>>Health</option>
            <option value="entertainment" <%= "entertainment".equals(expense.getCategory()) ? "selected" : "" %>>Entertainment</option>
            <option value="other" <%= "other".equals(expense.getCategory()) ? "selected" : "" %>>Other</option>
        </select><br/>

        <label for="date">Date:</label>
        <input type="date" name="date" value="<%= formattedDate %>" required /><br/>

        <input type="submit" value="Update Expense" />
    </form>

    <a href="addExpense.jsp">Back to Add Expense</a>
</body>
</html>
