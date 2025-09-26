package com.expense.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import com.expense.util.DatabaseConnection;
import com.model.Category;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;

public class ExpenseDao{
    private static final String ADD_CATEGORY = "INSERT INTO categories (name) VALUES (?)";
    private static final String GET_CATEGORIES = "SELECT * FROM categories";
    private static final String EDIT_CATEGORY = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?";
    private static final String REFRESH_CATEGORY = "DELETE FROM categories";

    public String getAddCategory(String name) throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(ADD_CATEGORY,Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString(1, name);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0) throw new SQLException("Creating category failed, no rows affected.");
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return String.valueOf(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating category failed, no ID obtained.");
                }
            }
        }
    }


    public java.util.List<Category> getCategories() throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_CATEGORIES);
        ) {
            ResultSet rs = stmt.executeQuery();
            java.util.List<Category> categories = new ArrayList<>();
            while(rs.next()) {
                categories.add(new Category(rs.getString("id"), rs.getString("name")));
            }
            return categories;
        }
    }
    public void getEditCategory(String name, String id) throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(EDIT_CATEGORY);
        ) {
            stmt.setString(1, name);
            stmt.setString(2, id);
            stmt.executeUpdate();
        }
    }   

    public void getDeleteCategory(String id) throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_CATEGORY);
        ) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public void getRefreshCategory() throws SQLException{

        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(REFRESH_CATEGORY);
        ) {
            stmt.executeUpdate();
        }
    }
    private static final String ADD_EXPENSE = "INSERT INTO expense (description, category_id, amount) VALUES (?, ?, ?)";
    private static final String GET_EXPENSES = "SELECT e.*, c.name as category_name FROM expense e LEFT JOIN categories c ON e.category_id = c.id";
    private static final String EDIT_EXPENSE = "UPDATE expense SET description = ?, category_id = ?, amount = ? WHERE id = ?";
    private static final String DELETE_EXPENSE = "DELETE FROM expense WHERE id = ?";

    public String getAddExpense(String description, int categoryId, int amount) throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(ADD_EXPENSE, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString(1, description);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, amount);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0) throw new SQLException("Creating expense failed, no rows affected.");
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return String.valueOf(generatedKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        }
    }

    public java.util.List<java.util.Map<String, Object>> getExpenses() throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_EXPENSES);
        ) {
            ResultSet rs = stmt.executeQuery();
            java.util.List<java.util.Map<String, Object>> expenses = new ArrayList<>();
            while(rs.next()) {
                java.util.Map<String, Object> expense = new java.util.HashMap<>();
                expense.put("id", rs.getInt("id"));
                expense.put("description", rs.getString("description"));
                expense.put("category_id", rs.getInt("category_id"));
                expense.put("amount", rs.getInt("amount"));
                expense.put("created_at", rs.getTimestamp("created_at"));
                expense.put("category_name", rs.getString("category_name"));
                expenses.add(expense);
            }
            return expenses;
        }
    }

    public void getEditExpense(int id, String description, int categoryId, int amount) throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(EDIT_EXPENSE);
        ) {
            stmt.setString(1, description);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, amount);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }

    public void getDeleteExpense(int id) throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_EXPENSE);
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
