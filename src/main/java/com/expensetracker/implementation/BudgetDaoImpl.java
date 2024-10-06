/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expensetracker.implementation;

/**
 *
 * @author USER
 */
import com.expensetracker.dao.BudgetDao;
import com.expensetracker.models.Budget;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class BudgetDaoImpl implements BudgetDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ExpenseTrackerPU");

    @Override
    public void setBudget(Budget budget) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(budget);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateBudget(Budget budget) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(budget);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
@Override
public Budget getCurrentBudget(int userId) {
    EntityManager entityManager = emf.createEntityManager();
    try {
        return entityManager.createQuery("SELECT b FROM Budget b WHERE b.user.id = :userId ORDER BY b.creationDate DESC", Budget.class)
                .setParameter("userId", userId)
                .setMaxResults(1) // Limit to one result
                .getSingleResult();
    } catch (NoResultException e) {
        return null; 
    } finally {
        entityManager.close();
    }
}



    @Override
    public List<Budget> getBudgetByUser(int userId) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<Budget> query = entityManager.createQuery(
                    "SELECT b FROM Budget b WHERE b.user.id = :userId",
                    Budget.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteBudget(int id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Budget budget = entityManager.find(Budget.class, id);
            if (budget != null) {
                entityManager.remove(budget);
            }
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void editBudget(int id, Budget updatedBudget) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Budget existingBudget = entityManager.find(Budget.class, id);
            if (existingBudget != null) {
                existingBudget.setAmount(updatedBudget.getAmount());
                existingBudget.setPeriod(updatedBudget.getPeriod());
                existingBudget.setAlertThreshold(updatedBudget.getAlertThreshold());
                existingBudget.setCreationDate(updatedBudget.getCreationDate());               
                entityManager.merge(existingBudget);
            }
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Budget getBudgetById(int id) {
        EntityManager entityManager = emf.createEntityManager();
        Budget budget = null;
        try {
            budget = entityManager.find(Budget.class, id);
        } finally {
            entityManager.close();
        }
        return budget;
    }

    @Override
    public List<Budget> getBudgetsForUser(int userId) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<Budget> query = entityManager.createQuery(
                    "SELECT b FROM Budget b WHERE b.user.id = :userId", Budget.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }
      public void addToBudget(int userId, double amount) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Budget currentBudget = getCurrentBudget(userId); // Get current budget
            if (currentBudget != null) {
                double newAmount = currentBudget.getAmount() + amount;
                currentBudget.setAmount(newAmount);
                entityManager.merge(currentBudget); // Update budget in the database
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e; // Handle exception as needed
        } finally {
            entityManager.close();
        }
    }

    public void deductFromBudget(int userId, double amount) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Budget currentBudget = getCurrentBudget(userId); // Get current budget
            if (currentBudget != null) {
                double newAmount = currentBudget.getAmount() - amount;
                currentBudget.setAmount(newAmount);
                entityManager.merge(currentBudget); // Update budget in the database
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e; // Handle exception as needed
        } finally {
            entityManager.close();
        }
    }
}
