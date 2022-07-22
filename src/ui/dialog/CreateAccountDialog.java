package ui.dialog;

import data.UserDao;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Stefan Sidlovsky
 */
public class CreateAccountDialog {
    private final UserDao userDao;

    public CreateAccountDialog(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createAccount() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 10));

        JLabel realNameLabel = new JLabel("Real name:");
        panel.add(realNameLabel);
        JTextField realNameField = new JTextField();
        panel.add(realNameField);

        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel);
        JTextField emailField = new JTextField();
        panel.add(emailField);

        JLabel phoneNumberLabel = new JLabel("Phone number:");
        panel.add(phoneNumberLabel);
        JTextField phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        JLabel dateOfBirthLabel = new JLabel("Date of birth (DD.MM.YYYY):");
        panel.add(dateOfBirthLabel);
        JTextField dateOfBirthField = new JTextField();
        panel.add(dateOfBirthField);

        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);
        JTextField usernameField = new JTextField();
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm your password:");
        panel.add(confirmPasswordLabel);
        JPasswordField confirmPasswordField = new JPasswordField();
        panel.add(confirmPasswordField);

        // TODO validation of input

        int option = JOptionPane.showConfirmDialog(null, panel, "Create account",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            String realName = realNameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthField.getText(), formatter);
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            User newUser = new User(username, password, realName, email, phoneNumber, dateOfBirth);
            userDao.create(newUser);
            JOptionPane.showMessageDialog(null, "Your account was successfully created\n" +
                    "Please log in");
        }

    }
}
