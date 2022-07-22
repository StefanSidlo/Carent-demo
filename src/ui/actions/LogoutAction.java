package ui.actions;

import data.CarDao;
import data.OrderDao;
import data.UserDao;
import ui.startPage.UserLogin;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class LogoutAction extends AbstractAction {
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final JFrame parentFrame;

    public LogoutAction(UserDao userDao, CarDao carDao, OrderDao orderDao, JFrame parentFrame) {
        super("Logout");
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.parentFrame = parentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int areYouSureQuestion = JOptionPane.showConfirmDialog(parentFrame, "Are you sure?");
        if (areYouSureQuestion == JOptionPane.YES_OPTION) {
            parentFrame.dispose();
            UserLogin welcomePage = new UserLogin(userDao, carDao, orderDao);
        }
    }
}
