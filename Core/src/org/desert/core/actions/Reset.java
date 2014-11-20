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
        id = "org.rteasy.actions.Reset"
)
@ActionRegistration(
        iconBase = "org/desert/core/images/Reset.png",
        displayName = "#CTL_Reset"
)
@ActionReferences({
    @ActionReference(path = "Toolbars/Desert", position = 400, name = "#CTL_Reset"),
    @ActionReference(path = "Menu/Simulate", position = 400, name = "#CTL_Reset")
})
public final class Reset implements ActionListener {

    
    public Reset(Simulator sim) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Simulator.reset();
    }
}
