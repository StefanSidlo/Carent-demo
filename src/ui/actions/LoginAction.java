package ui.actions;

import data.CarDao;
import data.OrderDao;
import data.UserDao;
import ui.homepage.AdminHomePage;
import ui.homepage.UserHomePage;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class LoginAction extends AbstractAction {
    private final JFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;

    public LoginAction(JFrame frame, JTextField usernameField, JPasswordField passwordField, UserDao userDao, CarDao carDao, OrderDao orderDao) {
        super("Login");
        this.frame = frame;
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        if (!userDao.verifyUser(username, password)) {
            JOptionPane.showMessageDialog(null, "Wrong Username & Password");
            return;
        }

        frame.dispose();

        if (username.equals("Admin")) {
            AdminHomePage adminHomePage = new AdminHomePage(userDao, carDao, orderDao, username);
        } else {
            UserHomePage homepage = new UserHomePage(userDao, carDao, orderDao, username);
        }

        JOptionPane.showMessageDialog(null, "You have successfully logged in");
    }
}
