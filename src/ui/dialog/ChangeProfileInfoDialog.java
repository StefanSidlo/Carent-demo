package ui.dialog;

import data.UserDao;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class ChangeProfileInfoDialog {
    private final String userName;
    private final UserDao userDao;
    private final JLabel realNameProfileLabel;
    private final JLabel emailProfileLabel;
    private final JLabel phoneNumberProfileLabel;

    public ChangeProfileInfoDialog(String userName, UserDao userDao, JLabel realNameProfileLabel,
                                   JLabel emailProfileLabel, JLabel phoneNumberProfileLabel) {
        this.userName = userName;
        this.userDao = userDao;
        this.realNameProfileLabel = realNameProfileLabel;
        this.emailProfileLabel = emailProfileLabel;
        this.phoneNumberProfileLabel = phoneNumberProfileLabel;
    }

    public void editProfileInfo() {
        User user = userDao.findUser(userName);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 0, 10));

        JLabel realNameLabel = new JLabel("Real name:");
        panel.add(realNameLabel);
        JTextField realNameField = new JTextField(user.getRealName(), 19);
        panel.add(realNameField);

        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel);
        JTextField emailField = new JTextField(user.getEmail(), 19);
        panel.add(emailField);

        JLabel phoneNumberLabel = new JLabel("Phone number:");
        panel.add(phoneNumberLabel);
        JTextField phoneNumberField = new JTextField(user.getPhoneNumber(), 19);
        panel.add(phoneNumberField);

        boolean invalidParameters = false;
        int option = JOptionPane.showConfirmDialog(null, panel, "Edit profile info",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            String newRealName = realNameField.getText();
            String newEmail = emailField.getText();
            String newPhoneNumber = phoneNumberField.getText();

            if (newRealName == null || newRealName.isEmpty()
                    || newEmail == null || newEmail.isEmpty()
                    || newPhoneNumber == null || newPhoneNumber.isEmpty()) {
                invalidParameters = true;
                JOptionPane.showMessageDialog(null, "Please fill in your information",
                        "Invalid parameters", JOptionPane.WARNING_MESSAGE);
            } else {
                userDao.editProfileInfo(userName, newRealName, newEmail, newPhoneNumber);
                realNameProfileLabel.setText(newRealName);
                emailProfileLabel.setText(newEmail);
                phoneNumberProfileLabel.setText(newPhoneNumber);
            }
        }
        if (invalidParameters) {
            editProfileInfo();
        }
    }
}
