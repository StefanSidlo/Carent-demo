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
public class CancelOrderAction extends AbstractAction {
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final String username;
    private final JTable availableCarsTable;
    private final JTable currentOrdersTable;
    private final JTable ordersHistoryTable;

    public CancelOrderAction(UserDao userDao, CarDao carDao, OrderDao orderDao, String username,
                             JTable availableCarsTable, JTable currentOrdersTable, JTable ordersHistoryTable) {
        super("Cancel order");
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.username = username;
        this.availableCarsTable = availableCarsTable;
        this.currentOrdersTable = currentOrdersTable;
        this.ordersHistoryTable = ordersHistoryTable;
    }

    private void cancelOrder(Order order, String carPlate) {
        // Deleting order from current order -> changing progress to CANCELED
        order.setProgress(OrderProgress.CANCELED);
        order.setFinalPrice(order.getRentPrice() + 50);
        orderDao.endOrder(order);
        OrdersTableModel newCurrentOrdersModel = new OrdersTableModel(username, orderDao, OrderProgress.IN_PROGRESS);
        currentOrdersTable.setModel(newCurrentOrdersModel);
        carDao.changeAvailability(carPlate, Availability.AVAILABLE);
    }

    private void payingFine(Order order) {
        // Paying fine for cancelling order
        int fine = order.getRentPrice() + 50;
        userDao.updateAccountMoney(username, -fine);
        User user = userDao.findUser(username);
        JOptionPane.showMessageDialog(currentOrdersTable, "Fine for cancelling order was taken from your account money\n" +
                "Fine: " + order.getFinalPrice() + " €" + "\n" + "Your current balance: " + user.getBalance() + " €");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentOrdersTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(null, "You have to select one order you want to cancel");
            return;
        }

        String carPlate = (String) currentOrdersTable.getValueAt(currentOrdersTable.getSelectedRow(), 0);
        String date = (String) currentOrdersTable.getValueAt(currentOrdersTable.getSelectedRow(), 3);
        String time = (String) currentOrdersTable.getValueAt(currentOrdersTable.getSelectedRow(), 4);

        Order order = orderDao.findOrder(username, carPlate, date, time);
        order.setEndTimestamp(Timestamp.valueOf(LocalDateTime.now()));

        // Check if order is within one hour of its creation
        if (order.getDurationInHours() > 1) {
            JOptionPane.showMessageDialog(null, "Order can not be canceled after one hour");
            return;
        }

        // Ask if user really wants to cancel order
        int answer = JOptionPane.showConfirmDialog(null, "Do you really want to cancel order?", "Select an Option...", JOptionPane.YES_NO_CANCEL_OPTION);
        if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CANCEL_OPTION) {
            return;
        }

        this.cancelOrder(order, carPlate);
        this.payingFine(order);

        // Updating order history table -> new finished order
        OrdersHistoryTableModel newOrdersHistoryTableModel = new OrdersHistoryTableModel(username, orderDao);
        ordersHistoryTable.setModel(newOrdersHistoryTableModel);

        //Updating table with available cars -> car is available again
        CarsTableModel newAvailableCarsModel = new CarsTableModel(carDao, Availability.AVAILABLE);
        availableCarsTable.setModel(newAvailableCarsModel);
    }
}
