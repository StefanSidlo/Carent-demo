package ui.actions;

import data.CarDao;
import ui.dialog.AddCarDialog;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import java.awt.event.ActionEvent;

/**
 * @author Stefan Sidlovsky
 */
public class AddCarAction extends AbstractAction {

    private final CarDao carDao;
    private final JTable allCarsTable;

    public AddCarAction(CarDao carDao, JTable allCarsTable) {
        super("Add car");
        this.carDao = carDao;
        this.allCarsTable = allCarsTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AddCarDialog dialog = new AddCarDialog(carDao, allCarsTable);
        dialog.addNewCar();
    }

}
