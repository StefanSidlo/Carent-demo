package ui.dialog;

import data.CarDao;
import model.Car;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class CarInfoDialog {
    private final String plate;
    private final CarDao carDao;
    private final JPanel mainPanel;

    public CarInfoDialog(String plate, CarDao carDao) {
        this.plate = plate;
        this.carDao = carDao;
        this.mainPanel = createPanel();
    }

    private JPanel createPanel() {
        Car car = carDao.findCar(plate);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(14, 2));

        createLabels(mainPanel, "Plate:", car.getPlate());
        createLabels(mainPanel, "Manufacturer:", car.getManufacturer());
        createLabels(mainPanel, "Model:", car.getModel());
        createLabels(mainPanel, "Year:", String.valueOf(car.getManYear()));
        createLabels(mainPanel, "Category:", car.getCategory());
        createLabels(mainPanel, "Seats:", String.valueOf(car.getSeats()));
        createLabels(mainPanel, "Transmission:", car.getTransmission().toString());
        createLabels(mainPanel, "Fuel:", car.getFuel().toString());
        createLabels(mainPanel, "4x4:", car.getFourXFour().toString());
        createLabels(mainPanel, "Power:", car.getPower() + " kW");
        createLabels(mainPanel, "Max speed:", car.getMaxSpeed() + " km/h");
        createLabels(mainPanel, "Price for rent:", car.getRentPrice() + " €");
        createLabels(mainPanel, "Price for hour:", car.getPriceForHour() + " €");
        createLabels(mainPanel, "Availability:", car.getAvailability().toString());

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
