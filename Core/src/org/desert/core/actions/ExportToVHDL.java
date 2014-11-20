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
import org.desert.gui.VHDLTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Design",
        id = "org.desert.core.actions.ExportToVHDL"
)
@ActionRegistration(
        displayName = "#CTL_ExportToVHDL"
)
@ActionReference(path = "Menu/Design", position = 3433)
@Messages("CTL_ExportToVHDL=Export to VHDL")
public final class ExportToVHDL implements ActionListener {

    private final Open context;

    public ExportToVHDL(Open context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String vhdlCode = Simulator.getVHDL("all");
        if (!vhdlCode.isEmpty()) {
            VHDLTopComponent vhdlTopCmpnt = (VHDLTopComponent) WindowManager.getDefault().findTopComponent("VHDLTopComponent");
            if (vhdlTopCmpnt == null) {
                vhdlTopCmpnt = new VHDLTopComponent();
            }

            org.openide.windows.Mode m = WindowManager.getDefault().findMode("right");
            m.dockInto(vhdlTopCmpnt);

            vhdlTopCmpnt.setVHDLCode(vhdlCode);

            if (!vhdlTopCmpnt.isOpened()) {
                vhdlTopCmpnt.open();
            }
        }
    }
}
