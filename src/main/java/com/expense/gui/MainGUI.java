package com.expense.gui;

import javax.swing.*;
import java.sql.Connection;
import com.model.Expense;
import com.model.Category;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import com.expense.util.DatabaseConnection;
import java.util.Date;


public class MainGUI extends JFrame {
    // Attributes
    private JPanel panel;
    private JButton category,expense;

    // Constructor
    public MainGUI() {

        initializeComponents();
        setupComponents();
        setupEventListeners();
    }

    private void initializeComponents() {
        setTitle("Expense Tracker");
        setVisible(true);
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void setupComponents() {
        setLayout(new BorderLayout());

        panel = new JPanel(new GridBagLayout()); // centers content both ways

        category = new JButton("Category");
        expense = new JButton("Expense");
        category.setPreferredSize(new Dimension(150,50));
        expense.setPreferredSize(new Dimension(150,50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // spacing between buttons

        // Add first button
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(category, gbc);

        // Add second button
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(expense, gbc);

        add(panel, BorderLayout.CENTER);
    }
    private void onSelectedExpense() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600,400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    private void onSelectedCategory() {
        JFrame frame = new JFrame("Category");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600,400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void setupEventListeners() {
        expense.addActionListener((ActionEvent e) -> {onSelectedExpense();});
        category.addActionListener((ActionEvent e) -> {onSelectedCategory();});
    }
}
