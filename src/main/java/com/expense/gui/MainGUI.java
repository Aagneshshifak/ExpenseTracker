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
import java.sql.SQLException;
import com.expense.DAO.ExpenseDao;




class CategoryGUI extends JFrame {
    // Attributes
    private JPanel panel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private ExpenseDao expenseDao;
    // Constructor
    public CategoryGUI() {
        expenseDao = new ExpenseDao();
        initializeComponents();
        setupComponents();
        setupEventListeners();
        loadCategories();
        setVisible(true);
    }
    private void updateCategories() {
        tableModel.setRowCount(0);
        try {
            java.util.List<Category> categories = expenseDao.getCategories();
            for(Category category : categories) {
                tableModel.addRow(new Object[]{category.getId(), category.getName()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void loadCategories() {
        updateCategories();
    }
    private void initializeComponents() {
       setTitle("category management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Name"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);
        add(tableScrollPane, BorderLayout.CENTER);

        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        addButton = new JButton("Add");
        refreshButton = new JButton("Refresh");

        nameField = new JTextField(20);
        nameLabel = new JLabel("Category Name:");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

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

        // Button panel for Edit, Delete, Refresh
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Table for displaying categories
        String[] columnNames = {"ID", "Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        categoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        add(panel);
    }
    private void setupEventListeners() {
        addButton.addActionListener((ActionEvent e) -> onAddCategory());
        editButton.addActionListener((ActionEvent e) -> onEditCategory());
        deleteButton.addActionListener((ActionEvent e) -> onDeleteCategory());
        refreshButton.addActionListener((ActionEvent e) -> onRefreshCategory());
    }
    private void onAddCategory() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String id = expenseDao.getAddCategory(name);
            Date createdAt = new Date();
            tableModel.addRow(new Object[]{id, name, createdAt});
            nameField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void onEditCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            expenseDao.getEditCategory(name, id);
            updateCategories();
            nameField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error editing category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
   private void onDeleteCategory() {
    int selectedRow = categoryTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        String id = (String) tableModel.getValueAt(selectedRow, 0);
        expenseDao.getDeleteCategory(id);
        updateCategories();
        nameField.setText("");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
   }

    private void onRefreshCategory() {
        updateCategories();
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
