package ui.actions;

import data.UserDao;

import ui.dialog.ProfileInfoDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class ShowProfileInfoAction extends AbstractAction {
    private final UserDao userDao;
    private final String userName;

    public ShowProfileInfoAction(UserDao userDao, String userName) {
        super("Profile info");
        this.userDao = userDao;
        this.userName = userName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProfileInfoDialog dialog = new ProfileInfoDialog(userName, userDao);
        dialog.show("Profile info");
    }
}
