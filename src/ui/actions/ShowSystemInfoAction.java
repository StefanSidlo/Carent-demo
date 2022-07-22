package ui.actions;

import data.CarDao;
import data.OrderDao;
import data.UserDao;

import ui.dialog.SystemInfoDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class ShowSystemInfoAction extends AbstractAction {
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;

    public ShowSystemInfoAction(UserDao userDao, CarDao carDao, OrderDao orderDao) {
        super("System info");
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SystemInfoDialog dialog = new SystemInfoDialog(userDao, carDao, orderDao);
        dialog.show("System info");
    }
}
