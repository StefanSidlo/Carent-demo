package ui.actions;

import data.CarDao;
import data.OrderDao;
import data.UserDao;
import model.Availability;
import model.Order;
import model.OrderProgress;
import ui.model.CarsTableModel;
import ui.model.OrdersTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Stefan Sidlovsky
 */
public class CreateOrderAction extends AbstractAction {
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final String username;
    private final JTable availableCarsTable;
    private final JTable currentOrdersTable;

    public CreateOrderAction(UserDao userDao, CarDao carDao, OrderDao orderDao, String username,
                             JTable availableCarsTable, JTable currentOrdersTable) {
        super("Create Order");
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.username = username;
        this.availableCarsTable = availableCarsTable;
        this.currentOrdersTable = currentOrdersTable;
    }

    private void newOrder(String carPlate, String orderAddress) {
        Order new_order = new Order(username, carPlate, Timestamp.valueOf(LocalDateTime.now()), orderAddress, userDao, carDao);
        orderDao.create(new_order);
        OrdersTableModel newCurrentOrdersModel = new OrdersTableModel(username, orderDao, OrderProgress.IN_PROGRESS);
        currentOrdersTable.setModel(newCurrentOrdersModel);
    }

    private void updateCarAndModel(String carPlate) {
        //Updating table with available cars -> car is no more available
        carDao.changeAvailability(carPlate, Availability.ORDERED);
        CarsTableModel newAvailableCarsModel = new CarsTableModel(carDao, Availability.AVAILABLE);
        availableCarsTable.setModel(newAvailableCarsModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (availableCarsTable.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "You have to select car to create order");
            return;
        }

        String carPlate = (String) availableCarsTable.getValueAt(availableCarsTable.getSelectedRow(), 5);

        if (orderDao.findAllByProgressAndUser(username, OrderProgress.IN_PROGRESS).size() >= 3) {
            JOptionPane.showMessageDialog(null, "Can not create order. You have already maximum " +
                    "amount of current orders.", "Maximum amount of orders", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Getting order address from user
        String orderAddress = JOptionPane.showInputDialog("Please, type address where the car will be delivered");
        if (orderAddress == null || orderAddress.isEmpty() || orderAddress.isBlank()) {
            JOptionPane.showMessageDialog(null, "You have to fill your order address!",
                    "Invalid input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.newOrder(carPlate, orderAddress);
        this.updateCarAndModel(carPlate);
    }
}
