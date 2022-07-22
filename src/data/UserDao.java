package data;

import model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * User database access class.
 *
 * @author Stefan Sidlovsky
 */
public final class UserDao implements DataAccessObject<User> {
    private final DataSource dataSource;
    private static final String SCHEMA_NAME = "APP";
    private static final String TABLE_NAME = "USERS";

    /**
     * Constructor of UserDao.
     *
     * @param dataSource A factory for connections to the physical data source.
     */
    public UserDao(DataSource dataSource){
        this.dataSource = dataSource;
        this.dropTable();
        User admin = new User("Admin", "adminpassword", "-",
                "-", "-", LocalDate.now());
        this.initTable(admin);
    }


    @Override
    public void create(User user) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement =  connection.prepareStatement(
                     "INSERT INTO USERS (USERNAME, PASSWORD, REAL_NAME, EMAIL, PHONE_NUMBER, " +
                             "DATE_OF_BIRTH, BALANCE, SINCE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRealName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhoneNumber());
            statement.setDate(6, Date.valueOf(user.getDateOfBirth()));
            statement.setInt(7, user.getBalance());
            statement.setDate(8, Date.valueOf(user.getSince()));
            statement.executeUpdate();
        } catch (SQLException ex){
            throw new DataAccessException("Failed to store user " + user, ex);
        }
    }

    /**
     * Creates instance of user from result of sql query.
     *
     * @param result Result set of sql query
     * @return User that was stored in database
     */
    private User getUserFromResultSet(ResultSet result){
        try {
            User loadedUser = new User(result.getString("USERNAME"),
                    result.getString("PASSWORD"),
                    result.getString("REAL_NAME"),
                    result.getString("EMAIL"),
                    result.getString("PHONE_NUMBER"),
                    result.getDate("DATE_OF_BIRTH").toLocalDate());
            loadedUser.setBalance(result.getInt("BALANCE"));
            loadedUser.setSince(result.getDate("SINCE").toLocalDate());
            return loadedUser;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load user from result set.", ex);
        }
    }

    /**
     * Finds user stored in database.
     *
     * @param username Username of stored user
     * @return Instance of user stored in database
     */
    public User findUser(String username){
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT USERNAME, PASSWORD, REAL_NAME, EMAIL, PHONE_NUMBER, DATE_OF_BIRTH, " +
                             "BALANCE, SINCE FROM USERS WHERE USERNAME = ?")) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return this.getUserFromResultSet(result);
            }
            else {
                throw new DataAccessException("User does not exists");
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find user " + username, ex);
        }
    }

    /**
     * Verifies if username and password are matching
     *
     * @param username Username of user
     * @param password Password of user
     * @return True if username and password match, otherwise false
     */
    public boolean verifyUser(String username, String password) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT USERNAME, PASSWORD FROM USERS " +
                             "WHERE USERNAME = ? and PASSWORD = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to verify user " + username, ex);
        }
    }

    /**
     * Checks if user is stored in database.
     *
     * @param username Username of user
     * @return True if user exists, otherwise false
     */
    public boolean userExists(String username){
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT USERNAME FROM USERS WHERE USERNAME = ?")) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to check if user exists " + username, ex);
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT USERNAME, PASSWORD, " +
                     "REAL_NAME, EMAIL, PHONE_NUMBER, DATE_OF_BIRTH, BALANCE, SINCE FROM USERS")) {
            List<User> users = new ArrayList<>();
            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {
                    User loadedUser = this.getUserFromResultSet(result);
                    if (!loadedUser.getUsername().equals("Admin")){
                        users.add(loadedUser);
                    }
                }
            }
            return users;
        } catch (SQLException ex){
            throw new DataAccessException("Failed to load all users", ex);
        }
    }

    /**
     * Computes balance of all users.
     *
     * @return Overall balance
     */
    public int getOverallBalance(){
        List<User> users = this.findAll();
        int result = 0;
        for (User user : users){
            result += user.getBalance();
        }
        return result;
    }

    /**
     * Edits attributes of user stored in database.
     *
     * @param username Identification, username of user
     * @param realName New real name of user
     * @param email New email of user
     * @param phoneNumber New phone number of user
     */
    public void editProfileInfo(String username, String realName, String email,
                                String phoneNumber){
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "UPDATE USERS SET REAL_NAME = ?, EMAIL = ?, PHONE_NUMBER = ? " +
                             "WHERE USERNAME = ?")) {
            st.setString(1, realName);
            st.setString(2, email);
            st.setString(3, phoneNumber);
            st.setString(4, username);
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to edit profile info " +
                        "of non-existing user: " + username);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to edit profile info of user: " + username, ex);
        }
    }

    @Override
    public void update(User entity) throws DataAccessException {

    }

    /**
     * Changes the password attribute of user stored in database.
     *
     * @param username Username of user with changed password
     * @param newPassword New password that will be stored in database
     */
    public void changePassword(String username, String newPassword) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "UPDATE USERS SET PASSWORD = ? WHERE USERNAME = ?")) {
            st.setString(1, newPassword);
            st.setString(2, username);
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update non-existing user: " + username);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update password of user: " + username, ex);
        }
    }

    /**
     * Changes the balance attribute of user stored in database.
     *
     * @param username Username of user with balance
     * @param amount Amount that will be added to user's balance
     */
    public void updateAccountMoney(String username, Integer amount){
        User user = this.findUser(username);
        int newBalance = user.getBalance() + amount;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     "UPDATE USERS SET BALANCE = ? WHERE USERNAME = ?")) {
            st.setInt(1, newBalance);
            st.setString(2, username);
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to add money " +
                        "to non-existing user: " + username);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update money of user: " + username, ex);
        }
    }

    @Override
    public void delete(User entity) throws DataAccessException {

    }

    public void deleteUser(String username){
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM USERS WHERE USERNAME = ?")) {
            st.setString(1, username);
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing user: " + username);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete user " + username, ex);
        }
    }

    private List<User> initUsers(){
        List<User> users = new ArrayList<>();
        String[] usernames = { "King66", "Casablanca", "RichBoy", "Tuna", "xPamx", "dEsTrOyEr666", "BestBoss", "Teeemp", "StanleyManley", "CatGirl", "NoGodPleaseNO", "Geniuz", "NardDog" };
        String[] realNames = { "Alex Small", "Sarah Oprah", "Belan Umtete", "Jim Halpert", "Pamela Halpert", "Dwight Schrute",
                "Michael Scott", "Ryan Howard", "Stanley Hudson", "Pamela Anderson", "Toby Flenderson", "Kevin Malone", "Andy Bernard" };
        int[] balances = { 200, 350, 600, 1000, 1000, 1200, 2000, 10, 300, 250, 100, 200, 800 };
        String[] sinces = { "22.02.2022", "05.11.2020", "10.03.2022", "06.06.2021", "06.06.2021", "18.09.2019", "15.04.2020",
                "26.05.2022", "01.01.2021", "18.09.2021", "08.11.2019", "05.03.2021", "05.08.2021" };
        for (int i = 0; i < 13; i++){
            LocalDate from = LocalDate.of(1959, 1, 1);
            LocalDate to = LocalDate.of(1990, 1, 1);
            long days = from.until(to, ChronoUnit.DAYS);
            long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
            LocalDate randomDate = from.plusDays(randomDays);
            User newUser = new User(usernames[i], "password", realNames[i], "random.email@gmail.com", "+421123456789", randomDate);
            newUser.setBalance(balances[i]);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate since = LocalDate.parse(sinces[i], formatter);
            newUser.setSince(since);

            users.add(newUser);
        }
        return users;
    }

    /**
     * Creates init users table with Admin as init user.
     * Also creates admin of the system.
     *
     * @param admin Admin of system
     */
    private void initTable(User admin) {
        if (!tableExits()) {
            createTable();
            create(admin);
            List<User> users = initUsers();
            for (User user : users){
                create(user);
            }
            User initUser = new User("user001", "password", "John Smith", "john.smith@gmail.com", "+421987654321", LocalDate.of(1995, 6, 6));
            initUser.setBalance(800);
            create(initUser);
        }
    }

    /**
     * Checks if users table already exists.
     *
     * @return True if table already exists, otherwise false
     */
    private boolean tableExits() {
        try (Connection connection = this.dataSource.getConnection();
             ResultSet rs = connection.getMetaData().getTables(null, SCHEMA_NAME, TABLE_NAME, null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " +
                    SCHEMA_NAME + "." + TABLE_NAME + " exist", ex);
        }
    }

    /**
     * Creates new table to the database.
     */
    private void createTable() {
        try (Connection connection = this.dataSource.getConnection();
             Statement st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.USERS (USERNAME VARCHAR(100), " +
                    "PASSWORD VARCHAR(100), REAL_NAME VARCHAR(100), EMAIL VARCHAR(100), " +
                    "PHONE_NUMBER VARCHAR(100), DATE_OF_BIRTH DATE, BALANCE INT, SINCE DATE)");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create USERS table", ex);
        }
    }

    /**
     * Removes users table from database.
     */
    public void dropTable() {
        try (Connection connection = this.dataSource.getConnection();
             Statement st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.USERS");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop USERS table", ex);
        }
    }
}
