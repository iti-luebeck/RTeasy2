/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.desert.core.Simulator;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(
        category = "Desert",
        id = "org.rteasy.actions.Run"
)
@ActionRegistration(
        iconBase = "org/desert/core/images/Run.png",
        displayName = "#CTL_Run"
)
@ActionReferences({
    @ActionReference(path = "Toolbars/Desert", position = 200, name = "#CTL_Run"),
    @ActionReference(path = "Menu/Simulate", position = 200, name = "#CTL_Run")
})
public final class Run implements ActionListener {

    
    public Run(Simulator sim) {
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Simulator.run();
    }
}
