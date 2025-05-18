package Presentation;

import BusinessLogic.ClientBLL;
import BusinessLogic.ProductBLL;
import BusinessLogic.OrderBLL;
import DataAccess.ClientDAO;
import DataAccess.ConnectionFactory;
import Model.Client;
import Model.Product;
import com.mysql.cj.xdevapi.Column;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;

public class View extends JFrame {
    private JPanel contentPane;
    private JPanel clientsPanel = new JPanel(new BorderLayout());
    private JPanel productsPanel = new JPanel(new BorderLayout());
    private JPanel ordersPanel = new JPanel(new BorderLayout());
    private Image background = new ImageIcon(getClass().getResource("/8.jpg")).getImage();
    private JMenuBar menuBar = new JMenuBar();
    private JButton clientsButton = new JButton("CLIENTS");
    private JButton productsButton = new JButton("PRODUCTS");
    private JButton ordersButton = new JButton("ORDERS");
    private JButton createOrder = new JButton("Create Order");
    private JButton addClient = new JButton("Add Client");
    private JButton addProduct = new JButton("Add Product");

    public View(String name) {
        super(name);
        this.setVisible(true);
        this.setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.prepareGUI();
    }

    public void prepareGUI() {
        contentPane = new BackgroundPanel(background);
        contentPane.setLayout(new BorderLayout());

        customizeMenuBar(menuBar);
        customizeMenuButtons(clientsButton);
        customizeMenuButtons(productsButton);
        customizeMenuButtons(ordersButton);

        customizeAddButtons(createOrder);
        customizeAddButtons(addClient);
        customizeAddButtons(addProduct);

        this.setActionListeners();

        menuBar.add(clientsButton);
        menuBar.add(productsButton);
        menuBar.add(ordersButton);
        menuBar.add(Box.createHorizontalGlue());


        contentPane.add(menuBar, BorderLayout.NORTH);
        this.setContentPane(contentPane);
    }

    public void setActionListeners() {
        clientsButton.addActionListener(e -> {
            displayClientsTable();
            contentPane.revalidate();
            contentPane.repaint();
        });

        productsButton.addActionListener(e -> {
            displayProductsTable();
            contentPane.revalidate();
            contentPane.repaint();
        });

        ordersButton.addActionListener(e -> {
            displayOrdersTable();
            contentPane.revalidate();
            contentPane.repaint();
        });

        addClient.addActionListener(e -> {
            displayAddClientDialog();
        });

        addProduct.addActionListener(e -> {
            displayAddProductDialog();
        });
    }

    public void customizeMenuBar(JMenuBar menuBar) {
        menuBar.setSize(800, 400);
        menuBar.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
    }

    public void customizeMenuButtons(JButton button) {
        button.setFont(new Font("Helvetica", Font.BOLD, 30));
        button.setPreferredSize(new Dimension(400, 70));
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.DARK_GRAY);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 3, 3, 3, Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    public void customizeAddButtons(JButton button) {
        button.setFont(new Font("Helvetica", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(40, 30));
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.DARK_GRAY);
    }

    public void displayClientsTable() {
        JTable clientsTable = ClientBLL.getClientsTable();
        customizeTable(clientsTable);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(addClient, BorderLayout.NORTH);
        clientsPanel.add(scrollPane, BorderLayout.CENTER);
        clearContentPane();
        contentPane.add(clientsPanel, BorderLayout.CENTER);
    }

    public void displayProductsTable() {
        JTable productsTable = ProductBLL.getProductsTable();
        customizeTable(productsTable);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        productsPanel.add(addProduct, BorderLayout.NORTH);
        productsPanel.add(scrollPane, BorderLayout.CENTER);
        clearContentPane();
        contentPane.add(productsPanel, BorderLayout.CENTER);
    }

    public void displayOrdersTable() {
        JTable ordersTable = OrderBLL.getOrdersTable();
        customizeTable(ordersTable);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        ordersPanel.add(createOrder, BorderLayout.NORTH);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        clearContentPane();
        contentPane.add(ordersPanel, BorderLayout.CENTER);
    }

    public void displayAddClientDialog() {
        JDialog dialog = new JDialog(this, "Add new client", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(8, 1));
        dialog.setLocationRelativeTo(this);

        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel phoneLabel = new JLabel("Phone:");

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();

        dialog.add(createRow(nameField, nameLabel));
        dialog.add(createRow(ageField, ageLabel));
        dialog.add(createRow(emailField, emailLabel));
        dialog.add(createRow(addressField, addressLabel));
        dialog.add(createRow(phoneField, phoneLabel));

        JButton addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            int age = Integer.parseInt(ageField.getText());
            String address = addressField.getText();
            String phone = phoneField.getText();
            if (name.isEmpty() || age <= 0 || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                Client newClient = new Client(name, age, email, address, phone);
                ClientBLL.addClient(newClient);
                JOptionPane.showMessageDialog(dialog, "Client added successfully!");
                dialog.dispose();
                displayClientsTable();
            }
        });

        dialog.add(new JPanel());
        dialog.add(addButton);
        dialog.setVisible(true);
    }

    public void displayAddProductDialog() {
        JDialog dialog = new JDialog(this, "Add new product", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 1));
        dialog.setLocationRelativeTo(this);

        JLabel nameLabel = new JLabel("Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel stockLabel = new JLabel("Stock:");

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        dialog.add(createRow(nameField, nameLabel));
        dialog.add(createRow(priceField, priceLabel));
        dialog.add(createRow(stockField, stockLabel));

        JButton addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            float price = Float.parseFloat(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            if (name.isEmpty() || price <= 0 || stock < 0 ) {
                JOptionPane.showMessageDialog(dialog, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                Product newProduct = new Product(name, price, stock);
                ProductBLL.addProduct(newProduct);
                JOptionPane.showMessageDialog(dialog, "Product added successfully!");
                dialog.dispose();
                displayProductsTable();
            }
        });

        dialog.add(new JPanel());
        dialog.add(addButton);
        dialog.setVisible(true);
    }


    public void clearContentPane() {
        contentPane.removeAll();
        contentPane.add(menuBar, BorderLayout.NORTH);
    }

    public void customizeTable(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        centerRenderer.setVerticalAlignment(DefaultTableCellRenderer.CENTER);

        Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = columns.nextElement();
            column.setCellRenderer(centerRenderer);
            column.setPreferredWidth(100);
        }

        table.setBackground(new Color(240, 245, 255));
        table.setForeground(new Color(0, 51, 102));
        table.setGridColor(new Color(192, 199, 217));
        table.setSelectionBackground(new Color(255, 140, 0));
        table.setSelectionForeground(new Color(255, 255, 255));
        table.setFont(new Font("Dialog", Font.PLAIN, 20));
        table.setRowHeight(25);

        JTableHeader header = table.getTableHeader();
        Font currentFont = header.getFont();
        Font newFont = currentFont.deriveFont(20f);
        header.setBackground( new Color(0, 89, 179));
        header.setForeground(new Color(255, 255, 255));
        header.setFont(newFont);


    }

    public JPanel createRow(JTextField textField, JLabel label){
        JPanel row = new JPanel(new GridLayout(1, 2));
        row.add(label);
        row.add(textField);
        row.setBackground(new Color(167, 207, 255));
        row.setOpaque(false);

        return row;
    }

    public void customizeInputLabel(JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(100, 70));
        label.setFont(new Font("Consolas", Font.BOLD, 24));
        label.setForeground(new Color(80, 0, 120));
        label.setBackground(new Color(167, 207, 255));
    }

    public void customizeInputText(JTextField textField){
        textField.setFont(new Font("Consolas", Font.BOLD, 24));
        textField.setForeground(new Color(80, 0, 120));
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

}
