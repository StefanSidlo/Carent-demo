package ui.dialog;

import data.UserDao;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class ChangePasswordDialog {
    private final UserDao userDao;
    private final String username;

    public ChangePasswordDialog(UserDao userDao, String username) {
        this.userDao = userDao;
        this.username = username;
    }

    public void changePassword() {
        User user = userDao.findUser(username);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 0, 10));

        JLabel passwordLabel = new JLabel("Current password:");
        panel.add(passwordLabel);
        JTextField passwordField = new JPasswordField(19);
        panel.add(passwordField);

        JLabel newPasswordLabel = new JLabel("New password:");
        panel.add(newPasswordLabel);
        JTextField newPasswordField = new JPasswordField(19);
        panel.add(newPasswordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm new password:");
        panel.add(confirmPasswordLabel);
        JTextField confirmPasswordField = new JPasswordField(19);
        panel.add(confirmPasswordField);

        boolean invalidInput = false;
        int option = JOptionPane.showConfirmDialog(null, panel, "Change password",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            String oldPassword = passwordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (oldPassword == null || newPassword == null || confirmPassword == null ||
                    oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() ||
                    oldPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
                invalidInput = true;
                JOptionPane.showMessageDialog(null, "Fill all fields!", "Invalid input",
                        JOptionPane.WARNING_MESSAGE);
            } else if (!oldPassword.equals(user.getPassword())) {
                invalidInput = true;
                JOptionPane.showMessageDialog(null, "Wrong account password!", "Incorrect password",
                        JOptionPane.WARNING_MESSAGE);
            } else if (!confirmPassword.equals(newPassword)) {
                invalidInput = true;
                JOptionPane.showMessageDialog(null, "Confirm password is not correct!", "Confirm password",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                userDao.changePassword(username, newPassword);
                JOptionPane.showMessageDialog(null, "Your password was successfully changed");
            }
        }
        if (invalidInput) {
            changePassword();
        }
    }
}
