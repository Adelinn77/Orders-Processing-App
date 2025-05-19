package Presentation;

import BusinessLogic.ClientBLL;
import BusinessLogic.ProductBLL;
import BusinessLogic.OrderBLL;
import Model.Client;
import Model.Orderr;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;

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
        });

        productsButton.addActionListener(e -> {
            displayProductsTable();
        });

        ordersButton.addActionListener(e -> {
            displayOrdersTable();
        });

        addClient.addActionListener(e -> {
            displayAddClientDialog();
        });

        addProduct.addActionListener(e -> {
            displayAddProductDialog();
        });

        createOrder.addActionListener(e -> {
            displayCreateOrderDialog();
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
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        clientsPanel.removeAll();
        JTable clientsTable = ClientBLL.getClientsTable();
        clientsTable.setComponentPopupMenu(popupMenu);
        clientsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = clientsTable.rowAtPoint(e.getPoint());
                clientsTable.setRowSelectionInterval(row, row);
            }
        });
        deleteItem.addActionListener(e -> {
            int selectedRow = clientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int clientId = (int) clientsTable.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(clientsTable, "Are you sure you want to delete this client?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Client client = ClientBLL.findClient(clientId);
                    ClientBLL.deleteClient(client);
                    displayClientsTable();
                }
            }
        });

        editItem.addActionListener(e -> {
            int selectedRow = clientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int clientId = (int) clientsTable.getValueAt(selectedRow, 0);
                Client client = ClientBLL.findClient(clientId);
                showEditClientDialog(client);
            }
        });
        customizeTable(clientsTable);
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(addClient, BorderLayout.NORTH);
        clientsPanel.add(scrollPane, BorderLayout.CENTER);
        clearContentPane();
        contentPane.add(clientsPanel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void displayProductsTable() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        productsPanel.removeAll();
        JTable productsTable = ProductBLL.getProductsTable();
        productsTable.setComponentPopupMenu(popupMenu);

        productsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = productsTable.rowAtPoint(e.getPoint());
                productsTable.setRowSelectionInterval(row, row);
            }
        });
        deleteItem.addActionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (int) productsTable.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(productsTable, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Product product = ProductBLL.findProduct(productId);
                    ProductBLL.deleteProduct(product);
                    displayProductsTable();
                }
            }
        });
        editItem.addActionListener(e -> {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int productId = (int) productsTable.getValueAt(selectedRow, 0);
                Product product = ProductBLL.findProduct(productId);
                showEditProductDialog(product);
            }
        });

        customizeTable(productsTable);
        JScrollPane scrollPane = new JScrollPane(productsTable);
        productsPanel.add(addProduct, BorderLayout.NORTH);
        productsPanel.add(scrollPane, BorderLayout.CENTER);
        clearContentPane();
        contentPane.add(productsPanel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void showEditProductDialog(Product product) {
        JDialog dialog = new JDialog(this, "Edit Product", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(4, 2));
        dialog.setLocationRelativeTo(this);

        JLabel nameLabel = new JLabel("Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel stockLabel = new JLabel("Stock:");

        JTextField nameField = new JTextField(product.getName());
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JTextField stockField = new JTextField(String.valueOf(product.getStock()));

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int stock = Integer.parseInt(stockField.getText());
                float price = Float.parseFloat(priceField.getText());

                if (name.isBlank() || stock < 0 || price < 0) {
                    throw new IllegalArgumentException("Invalid values");
                }

                product.setName(name);
                product.setStock(stock);
                product.setPrice(price);

                ProductBLL.updateProduct(product);

                JOptionPane.showMessageDialog(dialog, "Product updated!");
                dialog.dispose();
                displayProductsTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(nameLabel); dialog.add(nameField);
        dialog.add(stockLabel); dialog.add(stockField);
        dialog.add(priceLabel); dialog.add(priceField);
        dialog.add(new JLabel());
        dialog.add(saveButton);

        dialog.setVisible(true);
    }

    private void showEditClientDialog(Client client) {
        JDialog dialog = new JDialog(this, "Edit Client", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(6, 2));
        dialog.setLocationRelativeTo(this);

        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel phoneLabel = new JLabel("Phone:");

        JTextField nameField = new JTextField(client.getName());
        JTextField ageField = new JTextField(String.valueOf(client.getAge()));
        JTextField emailField = new JTextField(client.getEmail());
        JTextField addressField = new JTextField(client.getAddress());
        JTextField phoneField = new JTextField(client.getPhone());

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String email = emailField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();

                if (name.isBlank() || age <= 0 || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    throw new IllegalArgumentException("Invalid values");
                }

                client.setName(name);
                client.setAge(age);
                client.setEmail(email);
                client.setAddress(address);
                client.setPhone(phone);

                ClientBLL.updateClient(client);

                JOptionPane.showMessageDialog(dialog, "Client updated!");
                dialog.dispose();
                displayClientsTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(nameLabel); dialog.add(nameField);
        dialog.add(ageLabel); dialog.add(ageField);
        dialog.add(emailLabel); dialog.add(emailField);
        dialog.add(addressLabel); dialog.add(addressField);
        dialog.add(phoneLabel); dialog.add(phoneField);

        dialog.add(new JLabel());
        dialog.add(saveButton);
        dialog.setVisible(true);
    }

    public void displayOrdersTable() {

        ordersPanel.removeAll();
        JTable ordersTable = OrderBLL.getOrdersTable();
        customizeTable(ordersTable);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        ordersPanel.add(createOrder, BorderLayout.NORTH);
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        clearContentPane();
        contentPane.add(ordersPanel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
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

    public void displayCreateOrderDialog() {
        JDialog dialog = new JDialog(this, "Create order", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 1));
        dialog.setLocationRelativeTo(this);

        JLabel clientLabel = new JLabel("Select client:");
        JLabel productLabel = new JLabel("Select product:");
        JLabel quantityLabel = new JLabel("Quantity:");

        List<Product> products = ProductBLL.findAllProducts();
        List<Client> clients = ClientBLL.findAllClients();

        JComboBox<Client> clientComboBox = new JComboBox<>(clients.toArray(new Client[0]));
        JComboBox<Product> productComboBox = new JComboBox<>(products.toArray(new Product[0]));
        JTextField quantityField = new JTextField();

        productComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value != null ? value.getName() : ""));
        clientComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value != null ? value.getName() : ""));

        dialog.add(createRow(productComboBox, productLabel));
        dialog.add(createRow(clientComboBox, clientLabel));
        dialog.add(createRow(quantityField, quantityLabel));

        JButton addButton = new JButton("Create");

        addButton.addActionListener(e -> {
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            Client selectedClient = (Client) clientComboBox.getSelectedItem();

            try {
                int quantity = Integer.parseInt(quantityField.getText());
                if(quantity <= 0){
                    throw new NumberFormatException();
                }
                else if(selectedProduct == null){
                    JOptionPane.showMessageDialog(dialog, "Select product first!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(selectedClient == null){
                    JOptionPane.showMessageDialog(dialog, "Select client first!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(quantity > selectedProduct.getStock()){
                    JOptionPane.showMessageDialog(dialog, "Quantity exceeds stock!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(dialog, "Order created successfully!");
                    Orderr order = new Orderr(selectedClient.getId(), selectedProduct.getId(), quantity);
                    OrderBLL.addOrder(order);
                    selectedProduct.setStock(selectedProduct.getStock() - quantity);
                    ProductBLL.updateProduct(selectedProduct);
                    dialog.dispose();
                    displayOrdersTable();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Quantity must be a positive integer!", "Error", JOptionPane.ERROR_MESSAGE);
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

    public JPanel createRow(JComponent inputField, JLabel label){
        JPanel row = new JPanel(new GridLayout(1, 2));
        row.add(label);
        row.add(inputField);
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
