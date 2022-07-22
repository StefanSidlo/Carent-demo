import data.CarDao;
import data.OrderDao;
import data.UserDao;

import ui.startPage.UserLogin;

import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.awt.*;


/**
 * @author Stefan Sidlovsky
 */
public class Main {
    private static UserDao userDao;
    private static CarDao carDao;
    private static OrderDao orderDao;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            var dataSource = createDataSource();
            userDao = new UserDao(dataSource);
            carDao = new CarDao(dataSource);
            orderDao = new OrderDao(dataSource, userDao, carDao);
            EventQueue.invokeLater(() -> {
                try {
                    UserLogin frame = new UserLogin(userDao, carDao, orderDao);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static DataSource createDataSource() {
        String dbPath = System.getProperty("user.home") + "/pb175/db/car-rent";
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(dbPath);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

}
