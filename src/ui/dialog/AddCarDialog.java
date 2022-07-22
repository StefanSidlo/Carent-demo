package ui.dialog;

import data.CarDao;

import model.Availability;
import model.Car;
import model.FourXFour;
import model.Fuel;
import model.Transmission;

import ui.model.CarsTableModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class AddCarDialog {
    private final CarDao carDao;
    private final JTable allCarsTable;


    public AddCarDialog(CarDao carDao, JTable allCarsTable) {
        this.carDao = carDao;
        this.allCarsTable = allCarsTable;
    }

    public void addNewCar() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 0, 10));

        JLabel plateLabel = new JLabel("Plate:");
        panel.add(plateLabel);
        JTextField plateField = new JTextField(19);
        panel.add(plateField);

        JLabel manufacturerLabel = new JLabel("Manufacturer:");
        panel.add(manufacturerLabel);
        JTextField manufacturerField = new JTextField(19);
        panel.add(manufacturerField);

        JLabel modelLabel = new JLabel("Model:");
        panel.add(modelLabel);
        JTextField modelField = new JTextField(19);
        panel.add(modelField);

        JLabel yearLabel = new JLabel("Year:");
        panel.add(yearLabel);
        JTextField yearField = new JTextField(19);
        panel.add(yearField);

        JLabel categoryLabel = new JLabel("Category:");
        panel.add(categoryLabel);
        JTextField categoryField = new JTextField(19);
        panel.add(categoryField);

        JLabel seatsLabel = new JLabel("Seats:");
        panel.add(seatsLabel);
        String[] seatsAmountOptions = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        JComboBox seatsComboBox = new JComboBox(seatsAmountOptions);
        seatsComboBox.setSelectedIndex(0);
        panel.add(seatsComboBox);

        JLabel transmissionLabel = new JLabel("Transmission:");
        panel.add(transmissionLabel);
        String[] transmissionOptions = {"MANUAL", "AUTOMATIC"};
        JComboBox transmissionComboBox = new JComboBox(transmissionOptions);
        panel.add(transmissionComboBox);

        JLabel fuelLabel = new JLabel("Fuel:");
        panel.add(fuelLabel);
        String[] fuelOptions = {"DIESEL", "PETROL"};
        JComboBox fuelComboBox = new JComboBox(fuelOptions);
        panel.add(fuelComboBox);

        JLabel fourXFourLabel = new JLabel("4x4:");
        panel.add(fourXFourLabel);
        String[] fourXFourOptions = {"YES", "NO"};
        JComboBox fourXFourComboBox = new JComboBox(fourXFourOptions);
        panel.add(fourXFourComboBox);

        JLabel powerLabel = new JLabel("Power:");
        panel.add(powerLabel);
        JTextField powerField = new JTextField(19);
        panel.add(powerField);

        JLabel maxSpeedLabel = new JLabel("Max speed:");
        panel.add(maxSpeedLabel);
        JTextField maxSpeedField = new JTextField(19);
        panel.add(maxSpeedField);

        JLabel priceForRentLabel = new JLabel("Price for rent:");
        panel.add(priceForRentLabel);
        JTextField priceForRentField = new JTextField(19);
        panel.add(priceForRentField);

        JLabel priceForHourLabel = new JLabel("Price for hour:");
        panel.add(priceForHourLabel);
        JTextField priceForHourField = new JTextField(19);
        panel.add(priceForHourField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Add new car",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.NO_OPTION || option == JOptionPane.CLOSED_OPTION) {
            return;
        }

        String plate = plateField.getText();
        String manufacturer = manufacturerField.getText();
        String model = modelField.getText();
        int year = Integer.parseInt(yearField.getText());
        String category = categoryField.getText();
        int seats = Integer.parseInt(String.valueOf(seatsComboBox.getSelectedItem()));
        Transmission transmission = Transmission.fromDaoFormat(String.valueOf(transmissionComboBox.getSelectedItem()));
        Fuel fuel = Fuel.fromDaoFormat(String.valueOf(fuelComboBox.getSelectedItem()));
        FourXFour fourXFour = FourXFour.fromDaoFormat(String.valueOf(fourXFourComboBox.getSelectedItem()));
        int power = Integer.parseInt(powerField.getText());
        int maxSpeed = Integer.parseInt(maxSpeedField.getText());
        int priceForRent = Integer.parseInt(priceForRentField.getText());
        int priceForHour = Integer.parseInt(priceForHourField.getText());

        Car new_car = new Car(plate, manufacturer, model, year, category, seats, transmission, fuel, fourXFour, power,
                maxSpeed, priceForRent, priceForHour, Availability.AVAILABLE);
        carDao.create(new_car);

        CarsTableModel new_model = new CarsTableModel(carDao);
        allCarsTable.setModel(new_model);

        JOptionPane.showMessageDialog(null, "New car was added successfully");
    }

}
