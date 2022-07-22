package ui.actions;

import data.UserDao;
import ui.dialog.ChangePasswordDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class ChangePasswordAction extends AbstractAction {
    private final UserDao userDao;
    private final String username;

    public ChangePasswordAction(UserDao userdao, String username) {
        super("Change Password");
        this.userDao = userdao;
        this.username = username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChangePasswordDialog dialog = new ChangePasswordDialog(userDao, username);
        dialog.changePassword();
    }
}
