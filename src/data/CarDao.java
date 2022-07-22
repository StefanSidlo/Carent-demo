package data;

import model.Availability;
import model.Car;
import model.FourXFour;
import model.Fuel;
import model.Transmission;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Stefan Sidlovsky
 */
public class CarDao implements DataAccessObject<Car> {
    private final DataSource dataSource;

    public CarDao(DataSource dataSource){
        this.dataSource = dataSource;
        dropTable();
        initTable();
    }

    @Override
    public void create(Car car){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =  connection.prepareStatement(
                     "INSERT INTO CARS (PLATE, MANUFACTURER, MODEL, MAN_YEAR, CATEGORY, SEATS, TRANSMISSION, FUEL, " +
                             "FOUR_X_FOUR, POWER, MAX_SPEED, RENTPRICE, PRICEFORHOUR, AVAILABILITY) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, car.getPlate());
            statement.setString(2, car.getManufacturer());
            statement.setString(3, car.getModel());
            statement.setInt(4, car.getManYear());
            statement.setString(5, car.getCategory());
            statement.setInt(6, car.getSeats());
            statement.setString(7, car.getTransmission().toDaoFormat());
            statement.setString(8, car.getFuel().toDaoFormat());
            statement.setString(9, car.getFourXFour().toDaoFormat());
            statement.setInt(10, car.getPower());
            statement.setInt(11, car.getMaxSpeed());
            statement.setInt(12, car.getRentPrice());
            statement.setInt(13, car.getPriceForHour());
            statement.setString(14, car.getAvailability().toDaoFormat());
            statement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException("Failed to store car " + car, ex);
        }
    }

    public Car getCarFromResult(ResultSet result){
        try{
            return new Car(result.getString("PLATE"), result.getString("MANUFACTURER"),
                    result.getString("MODEL"), result.getInt("MAN_YEAR"),
                    result.getString("CATEGORY"), result.getInt("SEATS"),
                    Transmission.fromDaoFormat(result.getString("TRANSMISSION")),
                    Fuel.fromDaoFormat(result.getString("FUEL")),
                    FourXFour.fromDaoFormat(result.getString("FOUR_X_FOUR")),
                    result.getInt("POWER"), result.getInt("MAX_SPEED"),
                    result.getInt("RENTPRICE"), result.getInt("PRICEFORHOUR"),
                    Availability.fromDaoFormat(result.getString("AVAILABILITY")));
        } catch (SQLException ex){
            throw new DataAccessException("Failed to load car from result set", ex);
        }
    }


    public Car findCar(String plate){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT PLATE, MANUFACTURER, MODEL, MAN_YEAR, CATEGORY, SEATS, TRANSMISSION, FUEL, FOUR_X_FOUR, " +
                             "POWER, MAX_SPEED, RENTPRICE, PRICEFORHOUR, AVAILABILITY FROM CARS WHERE PLATE = ?")){
            statement.setString(1, plate);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return this.getCarFromResult(result);
            }
            else {
                throw new DataAccessException("Selected car does not exists");
            }
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load selected car", ex);
        }
    }


    @Override
    public List<Car> findAll(){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT PLATE, MANUFACTURER, MODEL, MAN_YEAR, " +
                     "CATEGORY, SEATS, TRANSMISSION, FUEL, FOUR_X_FOUR, POWER, MAX_SPEED, RENTPRICE, PRICEFORHOUR, " +
                     "AVAILABILITY FROM CARS")) {
            List<Car> cars = new ArrayList<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Car car = this.getCarFromResult(result);
                    cars.add(car);
                }
            }
            return cars;
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load all cars", ex);
        }
    }

    public int getNumberOfCars(){
        return findAll().size();
    }

    public List<Car> findAllByAvailability(Availability availability){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT PLATE, MANUFACTURER, MODEL, MAN_YEAR, " +
                     "CATEGORY, SEATS, TRANSMISSION, FUEL, FOUR_X_FOUR, POWER, MAX_SPEED, RENTPRICE, PRICEFORHOUR, " +
                     "AVAILABILITY FROM CARS WHERE AVAILABILITY = ?")) {
            statement.setString(1, availability.toDaoFormat());
            List<Car> cars = new ArrayList<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Car car = this.getCarFromResult(result);
                    cars.add(car);
                }
            }
            return cars;
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load cars by availability", ex);
        }
    }


    public void changeAvailability(String plate, Availability availability){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE CARS SET AVAILABILITY = ? WHERE PLATE = ?")) {
            statement.setString(1, availability.toDaoFormat());
            statement.setString(2, plate);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to order non-existing car: " + plate);
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Failed to order car " + plate, ex);
        }
    }

    @Override
    public void update(Car entity) throws DataAccessException {

    }

    @Override
    public void delete(Car entity) throws DataAccessException {

    }


    public void deleteCar(String plate) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM CARS WHERE PLATE = ?")) {
            statement.setString(1, plate);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing car: " + plate);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete car " + plate, ex);
        }
    }

    private List<Car> initCars(){
        List<Car> cars = new ArrayList<>();
        String[] manufacturers = { "Audi", "Lexus", "Mercedes", "Toyota", "BMW", "Subaru", "Subaru", "Porsche", "Porsche", "VW", "Lamborghini", "Alfa Romeo", "Škoda" };
        String[] models = { "A8", "LS", "Maybach GLS 600", "Corolla", "4 Series", "XV", "Impreza", "Cayman T", "Coyenne Turbo", "Arteon", "Aventador", "Giulia", "Felicia" };
        String[] category = { "Sedan", "Sedan", "Suv", "Sedan", "Coupe", "Crosstrek", "Hatchback", "Cabriolet", "Suv", "Sedan", "Supercar", "Sedan", "Hatchback" };
        int[] rentPrices = { 50, 60, 80, 15, 40, 25, 25, 60, 55, 20, 100, 30, 5 };
        int[] pricesForHour = { 10, 10, 30, 5, 15, 10, 10, 30, 30, 15, 30, 20, 5 };
        String[] plates = { "BL123AA", "BB459CK", "BL999AA", "KE623LK", "TT921ST", "BB537EF", "BB821FE", "BB666DV", "PP968LE", "LM444PA", "BL111ME", "ZA855HE", "BR867NO" };
        int[] manYears = { 2020, 2020, 2022, 2019, 2021, 2018, 2018, 2020, 2020, 2021, 2017, 2019, 1997 };
        int[] seats = { 5, 5, 7, 5, 5, 5, 5, 2, 2, 5, 2, 5, 5 };
        Transmission[] transmissions = { Transmission.AUTOMATIC, Transmission.AUTOMATIC, Transmission.AUTOMATIC,
                Transmission.AUTOMATIC, Transmission.AUTOMATIC, Transmission.AUTOMATIC, Transmission.MANUAL, Transmission.MANUAL,
                Transmission.AUTOMATIC, Transmission.MANUAL, Transmission.AUTOMATIC, Transmission.AUTOMATIC, Transmission.MANUAL };
        Fuel[] fuels = { Fuel.DIESEL, Fuel.DIESEL, Fuel.DIESEL, Fuel.PETROL, Fuel.PETROL, Fuel.PETROL, Fuel.PETROL, Fuel.PETROL,
                Fuel.PETROL, Fuel.DIESEL, Fuel.PETROL, Fuel.PETROL, Fuel.PETROL };
        FourXFour[] fourXfours = { FourXFour.YES, FourXFour.NO, FourXFour.YES, FourXFour.NO, FourXFour.YES, FourXFour.YES, FourXFour.YES,
                FourXFour.NO, FourXFour.NO, FourXFour.NO, FourXFour.NO, FourXFour.NO, FourXFour.NO };
        int[] powers = { 300, 300, 300, 110, 280, 150, 200, 350, 350, 100, 500, 200, 50 };
        int[] maxSpeeds = { 250, 250, 250, 200, 300, 180, 200, 300, 300, 180, 350, 200, 150 };
        for (int i = 0; i < 13; i++){
            Car newCar = new Car(plates[i], manufacturers[i], models[i], manYears[i], category[i], seats[i], transmissions[i],
                    fuels[i], fourXfours[i], powers[i], maxSpeeds[i], rentPrices[i], pricesForHour[i], Availability.AVAILABLE);
            cars.add(newCar);
        }
        return cars;
    }

    private void initTable() {
        if (!tableExits()) {
            createTable();
            initCars().forEach(this::create);
        }
    }

    private boolean tableExits() {
        try (Connection connection = dataSource.getConnection();
             ResultSet result = connection.getMetaData().getTables(null, "APP", "CARS", null)) {
            return result.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + "APP" + "." + "CARS" + " exist", ex);
        }
    }

    private void createTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE APP.CARS (PLATE VARCHAR(100), MANUFACTURER VARCHAR(100), " +
                    "MODEL VARCHAR(100), MAN_YEAR INT, CATEGORY VARCHAR(100), SEATS INT, TRANSMISSION VARCHAR (100), " +
                    "FUEL VARCHAR(100), FOUR_X_FOUR VARCHAR(100), POWER INT, MAX_SPEED INT, RENTPRICE INT, " +
                    "PRICEFORHOUR INT, AVAILABILITY VARCHAR(100))");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create CARS table", ex);
        }
    }

    public void dropTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DROP TABLE APP.CARS");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop CARS table", ex);
        }
    }

}
