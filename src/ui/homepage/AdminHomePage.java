package ui.homepage;

import ui.actions.ChangePasswordAction;
import ui.actions.LogoutAction;
import ui.actions.AddCarAction;
import ui.actions.DeleteCarAction;
import ui.actions.ShowCarInfoAction;
import ui.actions.ShowOrderInfoAction;
import ui.actions.ShowSystemInfoAction;
import ui.actions.ShowUserInfoAction;

import data.CarDao;
import data.OrderDao;
import data.UserDao;

import ui.model.CarsTableModel;
import ui.model.OrdersTableModel;
import ui.model.UsersTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class AdminHomePage {
    private final JFrame frame;
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final String usernameSession;
    private JTable allOrdersTable;

    public AdminHomePage(UserDao userDao, CarDao carDao, OrderDao orderDao, String usernameSession) {
        this.frame = createFrame();
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.usernameSession = usernameSession;
        frame.setJMenuBar(createMenuBar());
        frame.add(createTabbedPane());
    }

    public JTable getAllOrdersTable() {
        return allOrdersTable;
    }

    private void setAllOrdersTable(JTable allOrdersTable) {
        this.allOrdersTable = allOrdersTable;
    }

    private JTable createTable(AbstractTableModel model) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        JTable table = new JTable(model);
        table.setRowHeight(50);
        table.setShowGrid(true);

        table.setDefaultRenderer(Integer.class, centerRenderer);
        table.setDefaultRenderer(Float.class, centerRenderer);
        table.setDefaultRenderer(String.class, centerRenderer);

        ((DefaultTableCellRenderer) table
                .getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);
        return table;
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Homepage");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(350, 190, 1114, 597);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    private JMenu createMenu() {
        JMenu profileMenu = new JMenu("Admin");
        profileMenu.setMnemonic('p');
        profileMenu.add(new ShowSystemInfoAction(userDao, carDao, orderDao));
        profileMenu.add(new ChangePasswordAction(userDao, usernameSession));
        profileMenu.add(new LogoutAction(userDao, carDao, orderDao, frame));
        return profileMenu;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu());
        return menuBar;
    }

    private JPanel createContentPanel() {
        // Main content panel with list and buttons
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        return contentPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        buttonsPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        buttonsPanel.setBackground(Color.GRAY);
        return buttonsPanel;
    }

    private JPanel createCarsContentPanel() {
        // Main content panel with list and buttons
        JPanel contentPanel = createContentPanel();

        // Right panel with buttons
        JPanel buttonsPanel = createButtonsPanel();
        buttonsPanel.setLayout(new GridLayout(1, 3));

        // THIS WILL BE LIST
        JTable allCarsTable = createTable(new CarsTableModel(carDao));
        JScrollPane scrollPane = new JScrollPane(allCarsTable);

        // Buttons
        JButton showCarButton = new JButton("Show car info");
        showCarButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        showCarButton.addActionListener(new ShowCarInfoAction(carDao, allCarsTable));
        buttonsPanel.add(showCarButton);

        JButton addCarButton = new JButton("Add car");
        addCarButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        addCarButton.addActionListener(new AddCarAction(carDao, allCarsTable));
        buttonsPanel.add(addCarButton);

        JButton deleteCarButton = new JButton("Delete car");
        deleteCarButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        deleteCarButton.addActionListener(new DeleteCarAction(carDao, orderDao, allCarsTable, getAllOrdersTable()));
        buttonsPanel.add(deleteCarButton);


        // Adding list and right components
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createUsersContentPanel() {
        // Main content panel with list and buttons
        JPanel contentPanel = createContentPanel();

        // Right panel with buttons
        JPanel buttonsPanel = createButtonsPanel();
        buttonsPanel.setLayout(new GridLayout(1, 1));

        // This will be list
        JTable allUsersTable = createTable(new UsersTableModel(userDao));
        JScrollPane scrollPane = new JScrollPane(allUsersTable);

        // Buttons
        JButton showUserInfoButton = new JButton("Show user info");
        showUserInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        showUserInfoButton.addActionListener(new ShowUserInfoAction(userDao, allUsersTable));
        buttonsPanel.add(showUserInfoButton);


        // Adding list and right components
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createOrdersContentPanel() {
        // Main content panel with list and buttons
        JPanel contentPanel = createContentPanel();

        // Right panel with buttons
        JPanel buttonsPanel = createButtonsPanel();
        buttonsPanel.setLayout(new GridLayout(1, 1));

        // This will be list
        JTable allOrdersTable = createTable(new OrdersTableModel(orderDao));
        setAllOrdersTable(allOrdersTable);
        JScrollPane scrollPane = new JScrollPane(allOrdersTable);

        // Buttons
        JButton showOrderInfoButton = new JButton("Show order info");
        showOrderInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        showOrderInfoButton.addActionListener(new ShowOrderInfoAction(orderDao, allOrdersTable));
        buttonsPanel.add(showOrderInfoButton);

        // Adding list and right components
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        return contentPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel ordersPanel = createOrdersContentPanel();
        JPanel carsPanel = createCarsContentPanel();
        JPanel usersPanel = createUsersContentPanel();

        tabbedPane.addTab("Cars", carsPanel);
        tabbedPane.addTab("Users", usersPanel);
        tabbedPane.addTab("Orders", ordersPanel);

        return tabbedPane;
    }
}
