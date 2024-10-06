/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.expensetracker.dao;

/**
 *
 * @author USER
 */
import com.expensetracker.models.User;
import java.util.List;

public interface UserDao {
    void registerUser(User user) throws Exception ;
    User AuthUser(String username, String password)throws Exception ;
    User findUserById(int id);
    User findUserByEmail(String email) throws Exception;
    List<User> getAllUsers(); 
    User getUserById(int userId);
}