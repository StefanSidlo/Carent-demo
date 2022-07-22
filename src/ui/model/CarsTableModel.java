package ui.model;

import data.CarDao;

import model.Availability;
import model.Car;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author Stefan Sidlovsky
 */
public class CarsTableModel extends AbstractTableModel implements TableModel {
    private final List<Car> cars;

    public CarsTableModel(CarDao carDao, Availability availability){
        this.cars = carDao.findAllByAvailability(availability);
    }

    public CarsTableModel(CarDao carDao){
        this.cars = carDao.findAll();
    }

    private static final List<Column<Car, ?>> COLUMNS = List.of(
            new Column<>("Manufacturer", String.class, Car::getManufacturer),
            new Column<>("Model", String.class, Car::getModel),
            new Column<>("Category", String.class, Car::getCategory),
            new Column<>("Rent price", Integer.class, Car::getRentPrice),
            new Column<>("Price for hour", Integer.class, Car::getPriceForHour),
            new Column<>("Plate", String.class, Car::getPlate)
    );

    @Override
    public int getRowCount() {
        return cars.size();
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
        return COLUMNS.get(columnIndex).getValue(cars.get(rowIndex));
    }
}
