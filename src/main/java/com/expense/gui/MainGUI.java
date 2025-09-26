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


class CategoryGUI extends JFrame {
    // Attributes
    private JPanel panel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton addButton;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    // Constructor
    public CategoryGUI() {
        initializeComponents();
        setupComponents();
        setupEventListeners();
    }

    private void initializeComponents() {
        setTitle("Category Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void setupComponents() {
        panel = new JPanel(new BorderLayout());

        // Top panel for adding categories
        JPanel topPanel = new JPanel(new FlowLayout());
        nameLabel = new JLabel("Category Name:");
        nameField = new JTextField(20);
        addButton = new JButton("Add Category");
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(addButton);

        // Table for displaying categories
        String[] columnNames = {"ID", "Name", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0);
        categoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        add(panel);
    }

    private void onAddCategory() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Here you would typically add the category to the database
        // For demonstration, we'll just add it to the table with a dummy ID and current date
        int id = tableModel.getRowCount() + 1; // Dummy ID
        Date createdAt = new Date(); // Current date

        tableModel.addRow(new Object[]{id, name, createdAt});
        nameField.setText(""); // Clear input field
    }

    private void setupEventListeners() {
        addButton.addActionListener((ActionEvent e) -> onAddCategory());
    }
}
class ExpenseGUI extends JFrame { 
    // Attributes
    private JPanel panel;
    private JLabel nameLabel, amountLabel, categoryLabel;
    private JTextField nameField, amountField;
    private JComboBox<String> categoryComboBox;
    private JButton addButton;
    private JButton editButton, deleteButton;
    private JTable expenseTable;
    private DefaultTableModel tableModel;

    // Constructor
    public ExpenseGUI() {
        initializeComponents();
        setupComponents();
        setupEventListeners();
    }

    private void initializeComponents() {
        setTitle("Expense Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void setupComponents() {
        panel = new JPanel(new BorderLayout());

        // Top panel for adding expenses
        JPanel topPanel = new JPanel(new FlowLayout());
        nameLabel = new JLabel("Expense Name:");
        nameField = new JTextField(10);
        amountLabel = new JLabel("Amount:");
        amountField = new JTextField(10);
        categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>(); // Dummy categories
        addButton = new JButton("Add Expense");
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(amountLabel);
        topPanel.add(amountField);
        topPanel.add(categoryLabel);
        topPanel.add(categoryComboBox);
        topPanel.add(addButton);

        // Table for displaying expenses
        String[] columnNames = {"ID", "Name", "Amount", "Category", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0);
        expenseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        add(panel);
    }
    private void setupEventListeners() {
        //addButton.addActionListener((ActionEvent e) -> /*onAddExpense());

    }

}
    

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
        setSize(600,400); // Set size first
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center after size is set
        setVisible(true); // Show window last
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
    private void setupEventListeners() {
        expense.addActionListener((ActionEvent e) -> {onSelectedExpense();});
        category.addActionListener((ActionEvent e) -> {onSelectedCategory();});
    }

    private void onSelectedCategory() {
        new CategoryGUI(); // This window is already centered in its constructor    
}
    private void onSelectedExpense() {
        new ExpenseGUI(); // This window is already centered in its constructor
}
}
