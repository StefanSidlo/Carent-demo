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
public class DeleteAccountAction extends AbstractAction {
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CarDao carDao;
    private final String username;
    private final JFrame parentFrame;

    public DeleteAccountAction(UserDao userDao, OrderDao orderDao, CarDao carDao, String username, JFrame parentFrame) {
        super("Delete account");
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.carDao = carDao;
        this.username = username;
        this.parentFrame = parentFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int areYouSureQuestion = JOptionPane.showConfirmDialog(null, "Are you sure?");
        if (areYouSureQuestion == JOptionPane.YES_OPTION) {
            userDao.deleteUser(username);
            orderDao.deleteAllUserOrders(username);
            parentFrame.dispose();
            UserLogin welcomePage = new UserLogin(userDao, carDao, orderDao);
        }
    }
}
