package ui.actions;

import data.CarDao;
import data.OrderDao;

import model.Availability;
import model.Car;

import ui.model.CarsTableModel;
import ui.model.OrdersTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class DeleteCarAction extends AbstractAction {
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final JTable carsTable;
    private final JTable ordersTable;

    public DeleteCarAction(CarDao carDao, OrderDao orderDao, JTable carsTable, JTable ordersTable) {
        super("Delete car");
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.carsTable = carsTable;
        this.ordersTable = ordersTable;
    }

    private void deleteCar(String plate) {
        carDao.deleteCar(plate);
        orderDao.deleteAllCarOrders(plate);
        CarsTableModel newCarsModel = new CarsTableModel(carDao);
        carsTable.setModel(newCarsModel);
        OrdersTableModel newOrdersModel = new OrdersTableModel(orderDao);
        ordersTable.setModel(newOrdersModel);
        JOptionPane.showMessageDialog(null, "The car was successfully removed");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (carsTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(null, "You have to select one car");
            return;
        }

        String plate = (String) carsTable.getValueAt(carsTable.getSelectedRow(), 5);
        Car car = carDao.findCar(plate);
        if (car.getAvailability().equals(Availability.ORDERED)) {
            JOptionPane.showMessageDialog(null, "Car is currently ordered, thus can not be removed");
            return;
        }

        int response = JOptionPane.showConfirmDialog(null, "Are you sure?");
        if (response == JOptionPane.YES_OPTION) {
            this.deleteCar(plate);
        }
    }
}
