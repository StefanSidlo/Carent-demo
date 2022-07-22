package ui.homepage;

import ui.actions.ChangePasswordAction;
import ui.actions.LogoutAction;
import ui.actions.ShowProfileInfoAction;
import ui.actions.CancelOrderAction;
import ui.actions.CreateOrderAction;
import ui.actions.DeleteAccountAction;
import ui.actions.ReturnCarAction;
import ui.actions.ShowCarInfoAction;
import ui.actions.ShowOrderInfoAction;

import data.CarDao;
import data.OrderDao;
import data.UserDao;

import model.Availability;
import model.OrderProgress;

import ui.model.CarsTableModel;
import ui.model.OrdersTableModel;
import ui.model.OrdersHistoryTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Stefan Sidlovsky
 */
public class UserHomePage {
    private final JFrame frame;
    private final UserDao userDao;
    private final CarDao carDao;
    private final OrderDao orderDao;
    private final String usernameSession;
    private final JTable availableCarsTable;
    private final JTable currentOrdersTable;
    private final JTable ordersHistoryTable;

    public UserHomePage(UserDao userDao, CarDao carDao, OrderDao orderDao, String usernameSession) {
        this.frame = createFrame();
        this.userDao = userDao;
        this.carDao = carDao;
        this.orderDao = orderDao;
        this.usernameSession = usernameSession;
        this.availableCarsTable = createTable(new CarsTableModel(carDao, Availability.AVAILABLE));
        this.currentOrdersTable = createTable(new OrdersTableModel(usernameSession, orderDao, OrderProgress.IN_PROGRESS));
        this.ordersHistoryTable = createTable(new OrdersHistoryTableModel(usernameSession, orderDao));
        frame.setJMenuBar(createMenuBar());
        frame.add(createTabbedPane());
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

    public UserDao getUserDao() {
        return userDao;
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    private JMenu createMenu() {
        JMenu profileMenu = new JMenu("Profile");
        profileMenu.setMnemonic('p');
        profileMenu.add(new ShowProfileInfoAction(userDao, usernameSession));
        profileMenu.add(new ChangePasswordAction(userDao, usernameSession));
        profileMenu.add(new LogoutAction(userDao, carDao, orderDao, frame));
        profileMenu.add(new DeleteAccountAction(userDao, orderDao, carDao, usernameSession, frame));
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

    private JPanel createAvailableCarsContentPanel() {
        // Main content panel with list and buttons
        JPanel contentPanel = createContentPanel();

        // Right panel with buttons
        JPanel rightPanel = createButtonsPanel();
        rightPanel.setLayout(new GridLayout(1, 2));

        // List
        JScrollPane scrollPane = new JScrollPane(availableCarsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton showCarButton = new JButton("Show car");
        showCarButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        showCarButton.addActionListener(new ShowCarInfoAction(carDao, availableCarsTable));
        rightPanel.add(showCarButton);

        JButton createOrderButton = new JButton("Create Order");
        createOrderButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        createOrderButton.addActionListener(new CreateOrderAction(userDao, carDao, orderDao, usernameSession,
                availableCarsTable, currentOrdersTable));
        rightPanel.add(createOrderButton);

        // Adding components
        contentPanel.add(rightPanel, BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createCurrentOrdersPanel() {
        // Main content panel with list and buttons
        JPanel contentPanel = createContentPanel();

        // Right panel with buttons
        JPanel rightPanel = createButtonsPanel();
        rightPanel.setLayout(new GridLayout(1, 3));

        // List
        JScrollPane scrollPane = new JScrollPane(currentOrdersTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton cancelOrderButton = new JButton("Cancel Order");
        cancelOrderButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        cancelOrderButton.addActionListener(new CancelOrderAction(userDao, carDao, orderDao, usernameSession,
                availableCarsTable, currentOrdersTable, ordersHistoryTable));

        JButton returnCarButton = new JButton("Return car");
        returnCarButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        returnCarButton.addActionListener(new ReturnCarAction(userDao, carDao, orderDao, usernameSession,
                availableCarsTable, currentOrdersTable, ordersHistoryTable));

        JButton showOrderInfoButton = new JButton("Show order info");
        showOrderInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        showOrderInfoButton.addActionListener(new ShowOrderInfoAction(orderDao, currentOrdersTable));

        rightPanel.add(cancelOrderButton);
        rightPanel.add(returnCarButton);
        rightPanel.add(showOrderInfoButton);

        // Adding components
        contentPanel.add(rightPanel, BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createOrderHistoryPanel() {
        // Main content panel with list and buttons
        JPanel contentPanel = createContentPanel();

        // Right panel with buttons
        JPanel rightPanel = createButtonsPanel();
        rightPanel.setLayout(new GridLayout(1, 1));

        // List
        JScrollPane scrollPane = new JScrollPane(ordersHistoryTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton orderInfoButton = new JButton("Show order info");
        orderInfoButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        orderInfoButton.addActionListener(new ShowOrderInfoAction(orderDao, ordersHistoryTable));
        rightPanel.add(orderInfoButton);

        // Adding components
        contentPanel.add(rightPanel, BorderLayout.SOUTH);
        return contentPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel availableCarsPanel = createAvailableCarsContentPanel();
        JPanel currentOrdersPanel = createCurrentOrdersPanel();
        JPanel orderHistoryPanel = createOrderHistoryPanel();

        tabbedPane.addTab("Available cars", availableCarsPanel);
        tabbedPane.addTab("Current orders", currentOrdersPanel);
        tabbedPane.addTab("Order history", orderHistoryPanel);
        return tabbedPane;
    }
}
