package ui.actions;

import data.CarDao;

import ui.dialog.CarInfoDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class ShowCarInfoAction extends AbstractAction {
    private final CarDao carDao;
    private final JTable availableCarsTable;

    public ShowCarInfoAction(CarDao carDao, JTable availableCarsTable) {
        super("Show car info");
        this.carDao = carDao;
        this.availableCarsTable = availableCarsTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (availableCarsTable.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(null, "You have to select car");
            return;
        }

        String plate = (String) availableCarsTable.getValueAt(availableCarsTable.getSelectedRow(), 5);
        CarInfoDialog dialog = new CarInfoDialog(plate, carDao);
        dialog.show("Car info");
    }
}
