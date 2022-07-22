package ui.actions;

import data.UserDao;
import ui.dialog.CreateAccountDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class CreateAccountAction extends AbstractAction {
    private final UserDao userDao;

    public CreateAccountAction(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CreateAccountDialog dialog = new CreateAccountDialog(userDao);
        dialog.createAccount();
    }
}
