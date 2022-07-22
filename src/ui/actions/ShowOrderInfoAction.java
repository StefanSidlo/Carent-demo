package ui.actions;

import data.OrderDao;

import ui.dialog.OrderInfoDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class ShowOrderInfoAction extends AbstractAction {
    private final OrderDao orderDao;
    private final JTable table;

    public ShowOrderInfoAction(OrderDao orderDao, JTable table) {
        super("Show order info");
        this.orderDao = orderDao;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (table.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(null, "You have to select one order");
            return;
        }

        String plate = (String) table.getValueAt(table.getSelectedRow(), 0);
        String username = (String) table.getValueAt(table.getSelectedRow(), 2);
        String date = (String) table.getValueAt(table.getSelectedRow(), 3);
        String time = (String) table.getValueAt(table.getSelectedRow(), 4);
        OrderInfoDialog dialog = new OrderInfoDialog(plate, username, date, time, orderDao);
        dialog.show("Order info");
    }
}
