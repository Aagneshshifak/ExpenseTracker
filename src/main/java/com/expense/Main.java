package com.expense;

import com.expense.gui.MainGUI;
import com.expense.util.DatabaseConnection;

import java.sql.Connection;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try {
            Connection cn = db.getDBConnection();
            System.out.println("Connected to database successfully");
        }
        catch (Exception e) {
            System.out.println("Connection Failed");
            System.exit(1);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        SwingUtilities.invokeLater(
                ()->{
                    try {
                        new MainGUI().setVisible(true);
                    }
                    catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
        );
    }
}
