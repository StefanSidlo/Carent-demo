package ui.actions;

import data.UserDao;

import ui.dialog.UserInfoDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class ShowUserInfoAction extends AbstractAction {
    private final UserDao userDao;
    private final JTable table;

    public ShowUserInfoAction(UserDao userDao, JTable table) {
        super("Show user info");
        this.userDao = userDao;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (table.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(null, "You have to select user");
            return;
        }

        String username = (String) table.getValueAt(table.getSelectedRow(), 0);
        UserInfoDialog dialog = new UserInfoDialog(username, userDao);
        dialog.show("User info");
    }
}
