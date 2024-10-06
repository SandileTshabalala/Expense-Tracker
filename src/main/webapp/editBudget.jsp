<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.expensetracker.models.Budget" %>
<%@ page import="com.expensetracker.dao.BudgetDao" %>
<%@ page import="com.expensetracker.implementation.BudgetDaoImpl" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
    String budgetIdStr = request.getParameter("id");
    BudgetDao budgetDao = new BudgetDaoImpl();
    Budget budget = null;

    if (budgetIdStr != null && !budgetIdStr.isEmpty()) {
        try {
            int budgetId = Integer.parseInt(budgetIdStr);
            budget = budgetDao.getBudgetById(budgetId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    if (budget == null) {
        out.println("<h2>Invalid budget ID or budget not found</h2>");
        return;
    }

    // Format dates for input fields
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>

<html>
<head>
    <title>Edit Budget</title>
</head>
<body>
    <h1>Edit Budget</h1>
    <form action="BudgetServlet" method="post">
        <input type="hidden" name="action" value="edit" />
        <input type="hidden" name="budgetId" value="<%= budget.getId() %>" />

        <label for="amount">Budget Amount:</label>
        <input type="number" name="amount" value="<%= budget.getAmount() %>" required/><br/>

        <label for="period">Period:</label>
        <select name="period" required>
            <option value="Daily" <%= "Daily".equals(budget.getPeriod()) ? "selected" : "" %>>Daily</option>
            <option value="Weekly" <%= "Weekly".equals(budget.getPeriod()) ? "selected" : "" %>>Weekly</option>
            <option value="Monthly" <%= "Monthly".equals(budget.getPeriod()) ? "selected" : "" %>>Monthly</option>
        </select><br/>

        <label for="creationDate">Create Date:</label>
        <input type="date" name="creationDate" value="<%= dateFormat.format(budget.getCreationDate()) %>" required/><br/>

        <label for="alertThreshold">Alert Threshold (%):</label>
        <input type="number" name="alertThreshold" value="<%= budget.getAlertThreshold() %>" required/><br/>

        <input type="submit" value="Save Changes" />
    </form>

    <a href="dashboard.jsp">Back to Dashboard</a>
</body>
</html>
