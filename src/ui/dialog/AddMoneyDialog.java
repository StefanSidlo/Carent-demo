package ui.dialog;

import data.UserDao;

import javax.swing.*;

/**
 * @author Stefan Sidlovsky
 */
public class AddMoneyDialog {
    private final String userName;
    private final UserDao userDao;

    public AddMoneyDialog(String userName, UserDao userDao) {
        this.userName = userName;
        this.userDao = userDao;
    }

    public void show(String title) {
        Integer[] amountOptions = {50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 750, 1000, 1500, 2000};
        Integer amount = (Integer) JOptionPane.showInputDialog(null, "Enter amount:",
                title, JOptionPane.QUESTION_MESSAGE, null, amountOptions, amountOptions[0]);
        if (amount == null) {
            return;
        }
        userDao.updateAccountMoney(userName, amount);
        JOptionPane.showMessageDialog(null, amount + "€" + " was added to your account",
                "Successful bank connection", JOptionPane.INFORMATION_MESSAGE);
    }
}
