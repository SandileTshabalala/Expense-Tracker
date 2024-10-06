<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title> Register User</title>
    <link rel="stylesheet" href="styles.css"> 
</head>
<body>
    <h2>User Registration</h2>

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
        <input type="hidden" name="action" value="registerUser">
        
        <label for="username">Username:</label><br>
        <input type="text" id="username" name="username" required><br><br>
        
        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password" required><br><br>
        
        <label for="email">Email:</label><br>
        <input type="email" id="email" name="email" required><br><br>
        
        <label for="currency">Preferred Currency:</label><br>
        <select id="currency" name="currency">
            <option value="ZAR">ZAR</option>
            <option value="USD">USD</option>
            <option value="EUR">EUR</option>
            <option value="IDR">IDR</option>
            <!-- Add more currencies as needed -->
        </select><br><br>
        
        <button type="submit">Register</button>
    </form>
    
    <p>Already have an account? <a href="loginUser.jsp">Login here</a>.</p>
</body>
</html>
