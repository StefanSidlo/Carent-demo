package ui.dialog;

import data.CarDao;
import data.OrderDao;
import data.UserDao;

import model.Availability;
import model.OrderProgress;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class SystemInfoDialog {
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final JPanel mainPanel;

    public SystemInfoDialog(UserDao userDao, CarDao carDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.mainPanel = createPanel();
    }

    private JPanel createPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 2));

        createLabels(mainPanel, "Users:", String.valueOf(userDao.findAll().size()));
        createLabels(mainPanel, "Cars:", String.valueOf(carDao.getNumberOfCars()));
        createLabels(mainPanel, "Current orders:", String.valueOf(orderDao.findAllByProgress(OrderProgress.IN_PROGRESS).size()));
        createLabels(mainPanel, "Finished orders:", String.valueOf(orderDao.findAllByProgress(OrderProgress.FINISHED).size()));
        createLabels(mainPanel, "Canceled orders:", String.valueOf(orderDao.findAllByProgress(OrderProgress.CANCELED).size()));
        createLabels(mainPanel, "Available cars:", String.valueOf(carDao.findAllByAvailability(Availability.AVAILABLE).size()));
        createLabels(mainPanel, "Ordered cars:", String.valueOf(carDao.findAllByAvailability(Availability.ORDERED).size()));
        createLabels(mainPanel, "Overall profit:", userDao.getOverallBalance() + " €");

        return mainPanel;
    }

    private void createLabels(JPanel mainPanel, String labelString, String valueLabelString) {
        JLabel label = new JLabel(labelString);
        mainPanel.add(label);
        JLabel valueLabel = new JLabel(valueLabelString);
        mainPanel.add(valueLabel);
    }

    public void show(String title) {
        JOptionPane.showOptionDialog(null, mainPanel, title, JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
}
