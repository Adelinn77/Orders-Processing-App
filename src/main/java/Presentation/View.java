package Presentation;

import DataAccess.ConnectionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class View extends JFrame {
    private JPanel contentPane;
    private Image background = new ImageIcon(getClass().getResource("/w1.avif")).getImage();
    private JMenuBar menuBar = new JMenuBar();
    private JButton clientsButton = new JButton("CLIENTS");
    private JButton productsButton = new JButton("PRODUCTS");
    private JButton ordersButton = new JButton("ORDERS");

    public View(String name) {
        super(name);
        this.setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.prepareGUI();
    }

    public void prepareGUI() {
        contentPane = new BackgroundPanel(background);
        contentPane.setLayout(new BorderLayout());
        customizeMenuBar(menuBar);
        customizeMenuButtons(clientsButton);
        customizeMenuButtons(productsButton);
        customizeMenuButtons(ordersButton);
        menuBar.add(clientsButton);
        menuBar.add(productsButton);
        menuBar.add(ordersButton);
        menuBar.add(Box.createHorizontalGlue());
        contentPane.add(menuBar, BorderLayout.NORTH);
        this.setContentPane(contentPane);
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
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

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
