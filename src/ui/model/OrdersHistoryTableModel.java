package ui.model;

import data.OrderDao;

import model.Order;
import model.OrderProgress;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author Stefan Sidlovsky
 */
public class OrdersHistoryTableModel extends AbstractTableModel implements TableModel{
    private final List<Order> historyOrders;

    public OrdersHistoryTableModel(String username, OrderDao orderDao){
        this.historyOrders = orderDao.findAllFinishedByUser(username);
    }

    private static final List<Column<Order, ?>> COLUMNS = List.of(
            new Column<>("Plate", String.class, Order::getCarPlate),
            new Column<>("Model", String.class, Order::getModel),
            new Column<>("Username", String.class, Order::getUsername),
            new Column<>("Order date", String.class, Order::getOrderDate),
            new Column<>("Order time", String.class, Order::getOrderTime),
            new Column<>("Rent price", Integer.class, Order::getRentPrice),
            new Column<>("Price for hour", Integer.class, Order::getPriceForHour),
            new Column<>("Progress", String.class, Order::progressToString)
    );

    @Override
    public int getRowCount() {
        return historyOrders.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMNS.get(columnIndex).getColumnName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMNS.get(columnIndex).getColumnClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return COLUMNS.get(columnIndex).getValue(historyOrders.get(rowIndex));
    }
}
