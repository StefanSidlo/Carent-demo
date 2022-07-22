package ui.actions;

import data.UserDao;

import ui.dialog.AddMoneyDialog;

import javax.swing.*;

import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class AddMoneyAction extends AbstractAction {
    private final String userName;
    private final UserDao userDao;
    private final JLabel userBalanceLabel;

    public AddMoneyAction(String username, UserDao userDao, JLabel userBalanceLabel) {
        super("Add money");
        this.userName = username;
        this.userDao = userDao;
        this.userBalanceLabel = userBalanceLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AddMoneyDialog addMoneyDialog = new AddMoneyDialog(userName, userDao);
        addMoneyDialog.show("Add money");
        userBalanceLabel.setText(userDao.findUser(userName).getBalance() + " €");
    }
}
