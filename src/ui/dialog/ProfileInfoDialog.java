package ui.dialog;

import data.UserDao;

import model.User;

import ui.actions.AddMoneyAction;
import ui.actions.ChangeProfileInfoAction;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class ProfileInfoDialog {
    private final JPanel mainPanel;
    private final String userName;
    private final UserDao userDao;

    public ProfileInfoDialog(String userName, UserDao userDao) {
        this.userName = userName;
        this.userDao = userDao;
        this.mainPanel = createPanel();
    }

    private JLabel createLabels(JPanel mainPanel, String labelString, String valueLabelString) {
        JLabel label = new JLabel(labelString);
        mainPanel.add(label);
        JLabel valueLabel = new JLabel(valueLabelString);
        mainPanel.add(valueLabel);
        return valueLabel;
    }

    private JPanel createPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 2));
        User user = userDao.findUser(userName);

        createLabels(mainPanel, "Username: ", user.getUsername());

        JLabel userRealName = createLabels(mainPanel, "Real name: ", user.getRealName());;
        JLabel userEmail = createLabels(mainPanel, "Email: ", user.getEmail());;
        JLabel userPhoneNumber = createLabels(mainPanel, "Phone number: ", user.getPhoneNumber());;

        createLabels(mainPanel, "Date of birth: ", user.dateOfBirthToString());
        createLabels(mainPanel, "Since: ", user.sinceDateToString());

        JLabel userBalance = createLabels(mainPanel, "Balance: ", user.getBalance() + " €");;

        JButton addMoneyButton = new JButton("Add money");
        addMoneyButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        addMoneyButton.addActionListener(new AddMoneyAction(userName, userDao, userBalance));
        mainPanel.add(addMoneyButton);

        JButton changeProfileInfoButton = new JButton("Change profile info");
        changeProfileInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
        changeProfileInfoButton.addActionListener(new ChangeProfileInfoAction(userName, userDao,
                userRealName, userEmail, userPhoneNumber));
        mainPanel.add(changeProfileInfoButton);

        return mainPanel;
    }

    public void show(String title) {
        mainPanel.setPreferredSize(new Dimension(500, 200));
        JOptionPane.showOptionDialog(null, mainPanel, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
}
