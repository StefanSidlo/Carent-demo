package ui.actions;

import data.UserDao;
import ui.dialog.ChangeProfileInfoDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class ChangeProfileInfoAction extends AbstractAction {
    private final UserDao userDao;
    private final String userName;
    private final JLabel realNameProfileLabel;
    private final JLabel emailProfileLabel;
    private final JLabel phoneNumberProfileLabel;


    public ChangeProfileInfoAction(String userName, UserDao userDao, JLabel realNameProfileLabel,
                                   JLabel emailProfileLabel, JLabel phoneNumberProfileLabel) {
        super("Edit profile info");
        this.userName = userName;
        this.userDao = userDao;
        this.realNameProfileLabel = realNameProfileLabel;
        this.emailProfileLabel = emailProfileLabel;
        this.phoneNumberProfileLabel = phoneNumberProfileLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ChangeProfileInfoDialog dialog = new ChangeProfileInfoDialog(userName, userDao, realNameProfileLabel,
                emailProfileLabel, phoneNumberProfileLabel);
        dialog.editProfileInfo();
    }
}
