package ui.actions;

import data.CarDao;
import data.OrderDao;
import data.UserDao;

import model.Availability;
import model.Order;
import model.OrderProgress;
import model.User;

import ui.model.CarsTableModel;
import ui.model.OrdersTableModel;
import ui.model.OrdersHistoryTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Stefan Sidlovsky
 */
public class ReturnCarAction extends AbstractAction {
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final String username;
    private final JTable availableCarsTable;
    private final JTable currentOrdersTable;
    private final JTable ordersHistoryTable;

    public ReturnCarAction(UserDao userDao, CarDao carDao, OrderDao orderDao, String username,
                           JTable availableCarsTable, JTable currentOrdersTable, JTable ordersHistoryTable) {
        super("Return car");
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.username = username;
        this.availableCarsTable = availableCarsTable;
        this.currentOrdersTable = currentOrdersTable;
        this.ordersHistoryTable = ordersHistoryTable;
    }

    private void endOrder(String returnAddress, Order order) {
        // Deleting order from current order -> changing progress to finished
        order.setProgress(OrderProgress.FINISHED);
        order.setReturnAddress(returnAddress);
        int currentPrice = order.getCurrentPrice();
        order.setFinalPrice(currentPrice);
        orderDao.endOrder(order);
        OrdersTableModel newCurrentOrdersModel = new OrdersTableModel(username, orderDao, OrderProgress.IN_PROGRESS);
        currentOrdersTable.setModel(newCurrentOrdersModel);
    }

    private void payForRent(Order order) {
        // Paying for renting car
        userDao.updateAccountMoney(username, -order.getFinalPrice());
        User user = userDao.findUser(username);
        JOptionPane.showMessageDialog(null, "Thank you for using our services. Our team is on its way.\n" +
                "Price for rent: " + order.getFinalPrice() + " €" + "\n" + "Your current balance: " + user.getBalance() + " €");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentOrdersTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(null, "You have to select car that you want to return");
            return;
        }

        String carPlate = (String) currentOrdersTable.getValueAt(currentOrdersTable.getSelectedRow(), 0);
        String date = (String) currentOrdersTable.getValueAt(currentOrdersTable.getSelectedRow(), 3);
        String time = (String) currentOrdersTable.getValueAt(currentOrdersTable.getSelectedRow(), 4);

        // Check if car can be returned
        Order order = orderDao.findOrder(username, carPlate, date, time);
        order.setEndTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        if (order.getDurationInHours() < 4) {
            JOptionPane.showMessageDialog(null,
                    "Can not return car. Minimal time for rent is 3 hours", "Rent time",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Getting return address from user and setting order
        String returnAddress = JOptionPane.showInputDialog("Please, type your current address where the car will be returned");
        if (returnAddress == null) {
            return;
        } else if (returnAddress.isEmpty() || returnAddress.isBlank()) {
            JOptionPane.showMessageDialog(null, "Please fill your return address!",
                    "Invalid input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.endOrder(returnAddress, order);
        this.payForRent(order);

        // Updating order history table -> new finished order
        OrdersHistoryTableModel newOrdersHistoryTableModel = new OrdersHistoryTableModel(username, orderDao);
        ordersHistoryTable.setModel(newOrdersHistoryTableModel);

        //Updating table with available cars -> car is available again
        carDao.changeAvailability(carPlate, Availability.AVAILABLE);
        CarsTableModel newAvailableCarsModel = new CarsTableModel(carDao, Availability.AVAILABLE);
        availableCarsTable.setModel(newAvailableCarsModel);
    }
}
