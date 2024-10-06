<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="styles.css"> 
</head>
<body>
    <h2>User Login</h2>

    <!-- Display error messages if any -->
    <%
        String error = (String) request.getAttribute("error");
        if (error != null && !error.isEmpty()) {
    %>
        <div class="error"><%= error %></div>
    <%
        }
    %>

    <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="loginUser">

        <label for="email">Email:</label><br>
        <input type="email" id="email" name="email" required><br><br>

        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit">Login</button>
    </form>

    <p>Don't have an account? <a href="registerUser.jsp">Register here</a>.</p>
</body>
</html>
