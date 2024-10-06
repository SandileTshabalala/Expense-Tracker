/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expensetracker.implementation;

/**
 *
 * @author USER
 */
import com.expensetracker.dao.ExpenseDao;
import com.expensetracker.models.Expense;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseDaoImpl implements ExpenseDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ExpenseTrackerPU");

    @Override
    public void addExpense(Expense expense) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(expense);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Expense getExpenseById(int id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.find(Expense.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Expense> getExpensesByUser(int userId) {
        EntityManager entityManager = emf.createEntityManager();
        List<Expense> expenses = new ArrayList<>();
        try {
            TypedQuery<Expense> query = entityManager.createQuery(
                    "SELECT e FROM Expense e WHERE e.user.id = :userId ORDER BY e.date DESC",
                    Expense.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteExpense(int id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Expense expense = entityManager.find(Expense.class, id);
            if (expense != null) {
                entityManager.remove(expense);
            }
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void editExpense(int id, Expense expense) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Expense existingExpense = entityManager.find(Expense.class, id);
            if (existingExpense != null) {
                existingExpense.setAmount(expense.getAmount());
                existingExpense.setDescription(expense.getDescription());
                existingExpense.setCategory(expense.getCategory());
                existingExpense.setDate(expense.getDate());
                entityManager.merge(existingExpense);
            }
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public double getTotalExpensesForUserAndPeriod(int userId, String period) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<Double> query = entityManager.createQuery(
                    "SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :startDate AND :endDate",
                    Double.class);

            // Determine the date range based on the period
            java.util.Calendar cal = java.util.Calendar.getInstance();
            java.util.Date endDate = cal.getTime();
            switch (period.toLowerCase()) {
                case "daily":
                    cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
                    break;
                case "weekly":
                    cal.add(java.util.Calendar.WEEK_OF_YEAR, -1);
                    break;
                case "monthly":
                    cal.add(java.util.Calendar.MONTH, -1);
                    break;
                default:
                    cal.add(java.util.Calendar.MONTH, -1);
                    break;
            }
            java.util.Date startDate = cal.getTime();

            query.setParameter("userId", userId);
            query.setParameter("startDate", startDate, TemporalType.DATE);
            query.setParameter("endDate", endDate, TemporalType.DATE);

            Double total = query.getSingleResult();
            return total != null ? total : 0.0;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public double getTotalExpensesForUser(int userId) {
        EntityManager entityManager = emf.createEntityManager();
        String jpql = "SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId";
        TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
        query.setParameter("userId", userId);

        Double total = query.getSingleResult();

        return (total != null) ? total : 0.0;
    }

    public List<Expense> getExpenses(int userId, Date startDate, Date endDate) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createQuery("SELECT e FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :startDate AND :endDate", Expense.class)
                    .setParameter("userId", userId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Map<String, Double> generateExpenseReport(int userId, Date startDate, Date endDate) {
        List<Expense> expenses = getExpenses(userId, startDate, endDate);
        Map<String, Double> report = new HashMap<>();

        for (Expense expense : expenses) {
            report.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }

        return report;
    }

    public List<Expense> getUserExpenses(int userId) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<Expense> query = entityManager.createQuery(
                    "SELECT e FROM Expense e WHERE e.user.id = :userId ORDER BY e.date DESC", Expense.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

}
