package ui.dialog;

import data.UserDao;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class UserInfoDialog {
    private final String username;
    private final UserDao userDao;
    private final JPanel mainPanel;

    public UserInfoDialog(String username, UserDao userDao) {
        this.username = username;
        this.userDao = userDao;
        this.mainPanel = createPanel();
    }

    public JPanel createPanel() {
        User user = userDao.findUser(username);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 2));

        createLabels(mainPanel, "Username:", user.getUsername());
        createLabels(mainPanel, "Real name:", user.getRealName());
        createLabels(mainPanel, "Email", user.getEmail());
        createLabels(mainPanel, "Phone number:", user.getPhoneNumber());
        createLabels(mainPanel, "Date of birth:", user.dateOfBirthToString());
        createLabels(mainPanel, "Since:", user.sinceDateToString());
        createLabels(mainPanel, "Balance:", user.getBalance() + " €");

        return mainPanel;
    }

    private void createLabels(JPanel mainPanel, String labelString, String valueLabelString) {
        JLabel label = new JLabel(labelString);
        mainPanel.add(label);
        JLabel valueLabel = new JLabel(valueLabelString);
        mainPanel.add(valueLabel);
    }

    public void show(String title) {
        JOptionPane.showOptionDialog(null, mainPanel, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
}
