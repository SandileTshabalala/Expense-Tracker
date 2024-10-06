<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Logout</title>
</head>
<body>

<%
    // Invalidate session
    session.invalidate();
%>

<p>You have been logged out. <a href="loginUser.jsp">Login again</a></p>

</body>
</html>
