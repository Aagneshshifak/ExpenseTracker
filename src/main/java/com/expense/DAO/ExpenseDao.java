package com.expense.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import com.expense.util.DatabaseConnection;
public class ExpenseDao{
    private static final String ADD_CATEGORY = "INSERT INTO category (name) VALUES (?)";

    public String getAddCategory(String name) throws SQLException{
        try (
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(ADD_CATEGORY,Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString( 0,name);
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
}
