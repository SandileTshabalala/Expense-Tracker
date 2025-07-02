# Expense Tracker

A web-based application for tracking personal expenses built with Java EE technologies.

## Features

- User authentication and authorization
- Expense tracking and categorization
- MySQL database integration
- Secure password hashing using bcrypt
- Modern web interface with JavaServer Pages (JSP)
- RESTful API support with Jackson JSON processing
- Email integration capabilities

## Technology Stack

- Java 11
- Jakarta EE 10
- MySQL 8.0.26
- Hibernate ORM 6.2.5
- Jackson JSON Processing 2.15.2
- JSP/Servlets
- Maven for dependency management

## Prerequisites

- Java 11 or higher
- MySQL 8.0.26
- Apache Tomcat 10.x
- Maven 3.8.1 or higher

## Setup Instructions

1. Clone the repository using NetBeans:
   - Open NetBeans IDE
   - Go to Team > Git > Clone
   - In the Clone Repository dialog:
     - Enter the repository URL
     - Choose the destination directory (e.g., `c:\Users\sandi\OneDrive\Documents\NetBeansProjects\Expense-Tracker`)
     - Branch(master)
     - Click Next and follow the wizard to complete the cloning process
   
   Alternatively, clone using command line:
   ```bash
   git clone <repository-url>
   cd Expense-Tracker
   ```

2. Set up the database:
   - Create a MySQL database
   - Update the database connection settings in your application configuration

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Deploy to Tomcat:
   - Copy the generated WAR file from `target/ExpenseTracker.war` to your Tomcat webapps directory
   - Start Tomcat server

5. Access the application:
   Open your web browser and navigate to:
   ```
   http://localhost:8080/ExpenseTracker
   ```

## Project Structure

```
src/
├── main/
│   ├── java/         # Java source code
│   ├── resources/    # Configuration files
│   └── webapp/       # Web application files
│       ├── WEB-INF/  # Web application configuration
│       └── jsp/      # JSP pages
```

## Security Features

- Passwords are hashed using bcrypt
- Secure session management
- Input validation
- SQL injection prevention

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
