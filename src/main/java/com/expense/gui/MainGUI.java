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
        setSize(600, 400);
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
        addButton = new JButton("Add Category");
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

        // Input panel for category fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Category Name field
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Category Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        // Button panel for Add, Edit, Delete, Refresh
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Table for displaying categories
        String[] columnNames = {"ID", "Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        categoryTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);

        // Combine input and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        add(panel);
    }
    private void setupEventListeners() {
        addButton.addActionListener((ActionEvent e) -> onAddCategory());
        editButton.addActionListener((ActionEvent e) -> onEditCategory());
        deleteButton.addActionListener((ActionEvent e) -> onDeleteCategory());
        refreshButton.addActionListener((ActionEvent e) -> onRefreshCategory());
        
        // Add table selection listener
        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onCategoryTableSelection();
            }
        });
    }
    
    private void onCategoryTableSelection() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Populate field with selected category data
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            nameField.setText(name);
        }
    }
    private void onAddCategory() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String id = expenseDao.getAddCategory(name);
            tableModel.addRow(new Object[]{id, name});
            clearFields();
            JOptionPane.showMessageDialog(this, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
            clearFields();
            JOptionPane.showMessageDialog(this, "Category updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
        clearFields();
        JOptionPane.showMessageDialog(this, "Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
   }

    private void onRefreshCategory() {
        updateCategories();
    }
    
    private void clearFields() {
        nameField.setText("");
    }
}






class ExpenseGUI extends JFrame { 
    // Attributes
    private JPanel panel;
    private JLabel nameLabel, amountLabel, categoryLabel;
    private JTextField nameField, amountField;
    private JComboBox<Category> categoryComboBox;
    private JButton addButton;
    private JButton editButton, deleteButton, refreshButton;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private ExpenseDao expenseDao;

    // Constructor
    public ExpenseGUI() {
        expenseDao = new ExpenseDao();
        initializeComponents();
        setupComponents();
        setupEventListeners();
        loadCategories();
        loadExpenses();
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Expense Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        // Initialize components
        nameField = new JTextField(20);
        amountField = new JTextField(10);
        categoryComboBox = new JComboBox<>();
        addButton = new JButton("Add Expense");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
    }

    private void setupComponents() {
        panel = new JPanel(new BorderLayout());

        // Input section panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Expense Name
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Expense Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputPanel.add(nameField, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(amountField, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        inputPanel.add(categoryComboBox, gbc);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Table for displaying expenses
        String[] columnNames = {"ID", "Description", "Amount", "Category", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0);
        expenseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);

        // Combine input and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        add(panel);
    }
    private void setupEventListeners() {
        addButton.addActionListener((ActionEvent e) -> onAddExpense());
        editButton.addActionListener((ActionEvent e) -> onEditExpense());
        deleteButton.addActionListener((ActionEvent e) -> onDeleteExpense());
        refreshButton.addActionListener((ActionEvent e) -> onRefreshExpense());
        
        // Add table selection listener
        expenseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onTableSelection();
            }
        });
    }
    
    private void onTableSelection() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Populate fields with selected expense data
            String description = (String) tableModel.getValueAt(selectedRow, 1);
            String amount = String.valueOf(tableModel.getValueAt(selectedRow, 2));
            String categoryName = (String) tableModel.getValueAt(selectedRow, 3);
            
            nameField.setText(description);
            amountField.setText(amount);
            
            // Set the category in the combo box
            for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                Category category = categoryComboBox.getItemAt(i);
                if (category.getName().equals(categoryName)) {
                    categoryComboBox.setSelectedItem(category);
                    break;
                }
            }
        }
    }
    private void loadCategories() {
        try {
            java.util.List<Category> categories = expenseDao.getCategories();
            categoryComboBox.removeAllItems();
            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExpenses() {
        tableModel.setRowCount(0);
        try {
            java.util.List<java.util.Map<String, Object>> expenses = expenseDao.getExpenses();
            for (java.util.Map<String, Object> expense : expenses) {
                tableModel.addRow(new Object[]{
                    expense.get("id"),
                    expense.get("description"),
                    expense.get("amount"),
                    expense.get("category_name"),
                    expense.get("created_at")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddExpense() {
        String description = nameField.getText().trim();
        String amountText = amountField.getText().trim();
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        
        if (description.isEmpty() || amountText.isEmpty() || selectedCategory == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a category.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int amount = Integer.parseInt(amountText);
            String id = expenseDao.getAddExpense(description, Integer.parseInt(selectedCategory.getId()), amount);
            loadExpenses();
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEditExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String description = nameField.getText().trim();
        String amountText = amountField.getText().trim();
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        
        if (description.isEmpty() || amountText.isEmpty() || selectedCategory == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a category.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int expenseId = (Integer) tableModel.getValueAt(selectedRow, 0);
            int amount = Integer.parseInt(amountText);
            expenseDao.getEditExpense(expenseId, description, Integer.parseInt(selectedCategory.getId()), amount);
            loadExpenses();
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error editing expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int expenseId = (Integer) tableModel.getValueAt(selectedRow, 0);
            expenseDao.getDeleteExpense(expenseId);
            loadExpenses();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting expense: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRefreshExpense() {
        loadExpenses();
    }

    private void clearFields() {
        nameField.setText("");
        amountField.setText("");
        categoryComboBox.setSelectedIndex(0);
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
