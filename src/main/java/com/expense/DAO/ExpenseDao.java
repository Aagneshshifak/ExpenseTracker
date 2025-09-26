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
}
