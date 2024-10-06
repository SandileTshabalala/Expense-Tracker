/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expensetracker.implementation;

/**
 *
 * @author USER
 */
import com.expensetracker.dao.UserDao;
import com.expensetracker.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserDaoImpl implements UserDao {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ExpenseTrackerPU");

    @Override
    public void registerUser(User user) throws Exception {
        EntityManager entitymanager = emf.createEntityManager();
        EntityTransaction Entitytransaction = entitymanager.getTransaction();

        try {
            if (findUserByEmail(user.getEmail()) != null) {
                throw new Exception("Email already registered.");
            }
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            Entitytransaction.begin();
            entitymanager.persist(user);
            Entitytransaction.commit();
        } catch (Exception e) {
            if (Entitytransaction.isActive()) {
                Entitytransaction.rollback();
            }
            throw new Exception("Registration failed: " + e.getMessage(), e);
        } finally {
            entitymanager.close();
        }
    }

    @Override
    public User AuthUser(String email, String password) throws Exception {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            } else {
                throw new Exception("Invalid password.");
            }
        } catch (NoResultException e) {
            throw new Exception("Authentication failed: User not found.", e);
        } catch (Exception e) {
            throw new Exception("Authentication failed: " + e.getMessage(), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User findUserById(int id) {
        EntityManager entitymanager = emf.createEntityManager();
        try {
            return entitymanager.find(User.class, id);
        } finally {
            entitymanager.close();
        }
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        EntityManager entitymanager = emf.createEntityManager();
        try {
            TypedQuery<User> query = entitymanager.createQuery("SELECT v FROM User v WHERE v.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new Exception("Find by email failed: " + e.getMessage(), e);
        } finally {
            entitymanager.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User getUserById(int userId) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.find(User.class, userId);
        } finally {
            entityManager.close();
        }
    }
}
