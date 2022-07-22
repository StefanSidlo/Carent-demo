package ui.startPage;

import data.CarDao;
import data.OrderDao;
import data.UserDao;

import ui.actions.LoginAction;
import ui.actions.CreateAccountAction;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserLogin extends JFrame{

    private final JFrame frame;
    private final JPanel contentPanel;
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;

    public UserLogin(UserDao userDao, CarDao carDao, OrderDao orderDao) {
        this.frame = createFrame();
        this.contentPanel = createContentPanel();
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        createContent();
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Homepage");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 1200, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        frame.add(contentPanel);
        return contentPanel;
    }

    private void createLabel(String text, int x, int y, int height, int size) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.BLACK);
        label.setFont(new Font("Tahoma", Font.PLAIN, size));
        label.setBounds(x, y, 281, height);
        contentPanel.add(label);
    }

    private JTextField createTextField(int y) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(530, y, 281, 68);
        textField.setColumns(10);
        contentPanel.add(textField);
        return textField;
    }

    private JPasswordField createPasswordField(int y) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(530, y, 281, 68);
        contentPanel.add(passwordField);
        return passwordField;
    }

    private JButton createButton(String label, int x, int width) {
        JButton button = new JButton(label);
        button.setFont(new Font("Tahoma", Font.PLAIN, 26));
        button.setBounds(x, 400, width, 73);
        contentPanel.add(button);
        return button;
    }

    private void createContent() {
        createLabel("Login", 550, 20, 90, 50);
        createLabel("Username", 350, 150, 50, 30);
        createLabel("Password", 350, 250, 50, 30);

        JTextField loginField = createTextField(150);
        JPasswordField passwordField = createPasswordField(250);

        JButton loginButton = createButton("Login", 650, 162);
        loginButton.addActionListener(new LoginAction(frame, loginField, passwordField, userDao, carDao, orderDao));

        JButton createAccountButton = createButton("Create account", 350, 262);
        createAccountButton.addActionListener(new CreateAccountAction(userDao));
    }
}