/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.desert.core.Open;
import org.desert.core.Simulator;
import org.desert.gui.ControlSignalsTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Design",
        id = "org.desert.core.actions.ShowControlSignals"
)
@ActionRegistration(
        displayName = "#CTL_ShowControlSignals"
)
@ActionReference(path = "Menu/Design", position = 3333)
@Messages("CTL_ShowControlSignals=Show Control Signals")
public final class ShowControlSignals implements ActionListener {

    private final Open context;

    public ShowControlSignals(Open context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String signals = Simulator.getSignals();
        if (!signals.isEmpty()) {
            ControlSignalsTopComponent ctrlSgnlTopCmpnt = (ControlSignalsTopComponent) WindowManager.getDefault().findTopComponent("ControlSignalsTopComponent");
            if (ctrlSgnlTopCmpnt == null) {
                ctrlSgnlTopCmpnt = new ControlSignalsTopComponent();
            }

            org.openide.windows.Mode m = WindowManager.getDefault().findMode("right");
            m.dockInto(ctrlSgnlTopCmpnt);

            ctrlSgnlTopCmpnt.setControlSignals(signals);

            if (!ctrlSgnlTopCmpnt.isOpened()) {
                ctrlSgnlTopCmpnt.open();
            }
        }
    }
}
