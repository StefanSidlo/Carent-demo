package data;

import model.Availability;
import model.Order;
import model.OrderProgress;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Stefan Sidlovsky
 */
public final class OrderDao implements DataAccessObject<Order>{
    private final DataSource dataSource;
    private final UserDao userDao;
    private final CarDao carDao;

    public OrderDao(DataSource dataSource, UserDao userDao, CarDao carDao){
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.carDao = carDao;
        dropTable();
        initTable();
    }

    @Override
    public void create(Order order){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =  connection.prepareStatement(
                     "INSERT INTO ORDERS (USERNAME, CAR_PLATE, ORDER_DATE, " +
                             "END_DATE, DELIVERY_ADDRESS,RETURN_ADDRESS, PROGRESS, FINAL_PRICE) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, order.getUsername());
            statement.setString(2, order.getCarPlate());
            statement.setTimestamp(3, order.getOrderTimestamp());
            statement.setTimestamp(4, order.getEndTimestamp());
            statement.setString(5, order.getDeliveryAddress());
            statement.setString(6, order.getReturnAddress());
            statement.setString(7, order.getProgress().toDaoFormat());
            statement.setInt(8, order.getFinalPrice());
            statement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException("Failed to store " + order.toString(), ex);
        }
    }

    private Order getOrderFromResultSet(ResultSet result){
        try {
            Order new_order = new Order(result.getString("USERNAME"),
                    result.getString("CAR_PLATE"),
                    result.getTimestamp("ORDER_DATE"),
                    result.getString("DELIVERY_ADDRESS"), userDao, carDao);
            new_order.setProgress(OrderProgress.fromDaoFormat(
                    result.getString("PROGRESS")));
            new_order.setReturnAddress(result.getString("RETURN_ADDRESS"));
            new_order.setEndTimestamp(result.getTimestamp("END_DATE"));
            new_order.setFinalPrice(result.getInt("FINAL_PRICE"));
            return new_order;
        } catch (SQLException ex){
            throw new DataAccessException("Failed to load new order from sql result set", ex);
        }
    }

    @Override
    public List<Order> findAll(){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT USERNAME, CAR_PLATE," +
                     " ORDER_DATE, END_DATE, DELIVERY_ADDRESS, RETURN_ADDRESS, PROGRESS, FINAL_PRICE FROM ORDERS")) {
            List<Order> orders = new ArrayList<>();
            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Order loadedOrder = getOrderFromResultSet(result);
                    orders.add(loadedOrder);
                }
            }
            return orders;
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load all orders", ex);
        }
    }

    public List<Order> findAllByUser(String username){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT USERNAME, CAR_PLATE," +
                     " ORDER_DATE, END_DATE, DELIVERY_ADDRESS, RETURN_ADDRESS, PROGRESS, FINAL_PRICE " +
                     "FROM ORDERS WHERE USERNAME = ?")) {
            st.setString(1, username);
            List<Order> orders = new ArrayList<>();
            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    Order loadedOrder = getOrderFromResultSet(result);
                    orders.add(loadedOrder);
                }
            }
            return orders;
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load all orders of user:" + username, ex);
        }
    }

    public List<Order> findAllByProgress(OrderProgress progress){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT USERNAME, CAR_PLATE," +
                     " ORDER_DATE, END_DATE, DELIVERY_ADDRESS, RETURN_ADDRESS, PROGRESS, FINAL_PRICE " +
                     "FROM ORDERS WHERE PROGRESS = ?")) {
            statement.setString(1, progress.toDaoFormat());
            List<Order> orders = new ArrayList<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Order loadedOrder = getOrderFromResultSet(result);
                    orders.add(loadedOrder);
                }
            }
            return orders;
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load all orders", ex);
        }
    }

    public Order findOrder(String username, String plate, String date, String time){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT USERNAME, CAR_PLATE," +
                     " ORDER_DATE, END_DATE, DELIVERY_ADDRESS, RETURN_ADDRESS, PROGRESS, FINAL_PRICE " +
                     "FROM ORDERS WHERE USERNAME = ? AND CAR_PLATE = ?")) {
            statement.setString(1, username);
            statement.setString(2, plate);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Order order =  getOrderFromResultSet(result);
                if (order.getOrderDate().equals(date) && order.getOrderTime().equals(time)){
                    return order;
                }
            }
            throw new DataAccessException("Order does not exists");
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load order", ex);
        }
    }

    public List<Order> findAllFinishedByUser(String username){
        List<Order> progress1List = findAllByProgressAndUser(username, OrderProgress.FINISHED);
        List<Order> progress2List = findAllByProgressAndUser(username, OrderProgress.CANCELED);
        return Stream.of(progress1List, progress2List)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<Order> findAllByProgressAndUser(String username, OrderProgress progress){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT USERNAME, CAR_PLATE," +
                     " ORDER_DATE, END_DATE, DELIVERY_ADDRESS, RETURN_ADDRESS, PROGRESS, FINAL_PRICE FROM ORDERS " +
                     "WHERE USERNAME = ? AND PROGRESS = ?")) {
            statement.setString(1, username);
            statement.setString(2, progress.toDaoFormat());
            List<Order> orders = new ArrayList<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Order new_order = this.getOrderFromResultSet(result);
                    orders.add(new_order);
                }
            }
            return orders;
        } catch (SQLException | DataAccessException ex){
            throw new DataAccessException("Failed to load all orders", ex);
        }
    }

    public void endOrder(Order order){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE ORDERS SET PROGRESS = ?, END_DATE = ?, RETURN_ADDRESS = ?, FINAL_PRICE = ? " +
                             "WHERE USERNAME = ? AND CAR_PLATE = ?")) {
            statement.setString(1, order.getProgress().toDaoFormat());
            statement.setTimestamp(2, order.getEndTimestamp());
            statement.setString(3, order.getReturnAddress());
            statement.setInt(4, order.getFinalPrice());
            statement.setString(5, order.getUsername());
            statement.setString(6, order.getCarPlate());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to end non-existing order: " + order);
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Failed to end order: " + order);
        }
    }

    @Override
    public void update(Order entity) throws DataAccessException {

    }

    @Override
    public void delete(Order entity) throws DataAccessException {

    }

    public void deleteAllUserOrders(String username){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM ORDERS WHERE USERNAME = ?")) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Failed to delete all orders of user " + username, ex);
        }
    }

    public void deleteAllCarOrders(String carPlate){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM ORDERS WHERE CAR_PLATE = ?")) {
            statement.setString(1, carPlate);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete all orders of car " + carPlate, ex);
        }
    }

    private List<Order> initOrders(){
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order("user001", "PP968LE", Timestamp.valueOf(LocalDateTime.of(2022, 6, 18, 6, 30)), "Street 10", userDao, carDao);
        carDao.changeAvailability("PP968LE", Availability.ORDERED);
        orders.add(order1);
        Order order2 = new Order("user001", "ZA855HE", Timestamp.valueOf(LocalDateTime.of(2022, 5,15,9,10)), "Street 55", userDao, carDao);
        order2.setProgress(OrderProgress.FINISHED);
        order2.setFinalPrice(120);
        order2.setReturnAddress("Street 88");
        orders.add(order2);
        Order order3 = new Order("Tuna", "BL999AA", Timestamp.valueOf(LocalDateTime.of(2022, 6,17,15,45)), "Street 81", userDao, carDao);
        carDao.changeAvailability("BL999AA", Availability.ORDERED);
        orders.add(order3);
        Order order4 = new Order("Tuna", "BL999AA", Timestamp.valueOf(LocalDateTime.of(2022, 5,12,10,0)), "Street 81", userDao, carDao);
        order4.setProgress(OrderProgress.CANCELED);
        orders.add(order4);
        Order order5 = new Order("BestBoss", "BR867NO", Timestamp.valueOf(LocalDateTime.of(2022, 6,14,18,32)), "Street 42", userDao, carDao);
        order5.setProgress(OrderProgress.FINISHED);
        order5.setFinalPrice(250);
        orders.add(order5);
        Order order6 = new Order("Teeemp", "BB821FE", Timestamp.valueOf(LocalDateTime.of(2022, 6,3,14,23)), "Street 16", userDao, carDao);
        order6.setProgress(OrderProgress.FINISHED);
        order6.setFinalPrice(80);
        order6.setReturnAddress("Street 99");
        orders.add(order6);
        Order order7 = new Order("CatGirl", "BL111ME", Timestamp.valueOf(LocalDateTime.of(2022, 4,20,9,10)), "Street 18", userDao, carDao);
        order7.setProgress(OrderProgress.FINISHED);
        order7.setFinalPrice(380);
        orders.add(order7);
        return orders;
    }

    private void initTable() {
        if (!tableExits()) {
            createTable();
            initOrders().forEach(this::create);
        }
    }

    private boolean tableExits() {
        try (Connection connection = dataSource.getConnection();
             ResultSet result = connection.getMetaData().getTables(null, "APP", "ORDERS", null)) {
            return result.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " +
                    "APP" + "." + "ORDERS" + " exist", ex);
        }
    }

    private void createTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE APP.ORDERS (USERNAME VARCHAR(100), " +
                    "CAR_PLATE VARCHAR(100), ORDER_DATE TIMESTAMP, END_DATE TIMESTAMP, " +
                    "DELIVERY_ADDRESS VARCHAR(100), RETURN_ADDRESS VARCHAR(100), " +
                    "PROGRESS VARCHAR(100), FINAL_PRICE INT)");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create ORDERS table", ex);
        }
    }

    public void dropTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DROP TABLE APP.ORDERS");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop ORDERS table", ex);
        }
    }

}
