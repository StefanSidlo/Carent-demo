package ui.dialog;

import data.OrderDao;
import model.Car;
import model.Order;
import model.OrderProgress;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Stefan Sidlovsky
 */
public class OrderInfoDialog {
    private final String plate;
    private final String username;
    private final String date;
    private final String time;
    private final OrderDao orderDao;
    private final JPanel mainPanel;

    public OrderInfoDialog(String plate, String username, String date, String time, OrderDao orderDao) {
        this.plate = plate;
        this.username = username;
        this.date = date;
        this.time = time;
        this.orderDao = orderDao;
        this.mainPanel = createPanel();
    }

    private JPanel createPanel() {
        Order order = orderDao.findOrder(username, plate, date, time);
        order.setEndTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        Car car = order.getCar();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(13, 2));

        createLabels(mainPanel, "Plate:", car.getPlate());
        createLabels(mainPanel, "Model:", car.getModel());
        createLabels(mainPanel, "Username:", order.getUsername());
        createLabels(mainPanel, "Order date:", order.getOrderDate());
        createLabels(mainPanel, "Order time:", order.getOrderTime());
        createLabels(mainPanel, "Order address:", order.getDeliveryAddress());
        if (order.getProgress().equals(OrderProgress.FINISHED)) {
            createLabels(mainPanel, "Return date:", order.getEndDate());
            createLabels(mainPanel, "Return time:", order.getEndTime());
            createLabels(mainPanel, "Return address:", order.getReturnAddress());
        }
        createLabels(mainPanel, "Rent price:", order.getRentPrice() + " €");
        createLabels(mainPanel, "Price for hour:", order.getPriceForHour() + " €");
        switch (order.getProgress()) {
            case IN_PROGRESS:
                createLabels(mainPanel, "Current price:", order.getCurrentPrice() + " €");
                break;
            case FINISHED:
                createLabels(mainPanel, "Final price:", order.getFinalPrice() + " €");
                break;
            case CANCELED:
                createLabels(mainPanel, "Final price (fine):", order.getFinalPrice() + " €");
                break;
            default:
                break;
        }
        createLabels(mainPanel, "Progress:", order.getProgress().toString());

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
