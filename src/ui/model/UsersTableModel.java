package ui.model;

import data.UserDao;

import model.User;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author Stefan Sidlovsky
 */
public class UsersTableModel extends AbstractTableModel implements TableModel {
    private final List<User> users;

    public UsersTableModel(UserDao userDao){
        this.users = userDao.findAll();
    }

    private static final List<Column<User, ?>> COLUMNS = List.of(
            new Column<>("Username", String.class, User::getUsername),
            new Column<>("Real name", String.class, User::getRealName),
            new Column<>("Balance", Integer.class, User::getBalance),
            new Column<>("Since:", String.class, User::sinceDateToString)
    );

    @Override
    public int getRowCount() {
        return users.size();
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
        return COLUMNS.get(columnIndex).getValue(users.get(rowIndex));
    }
}
