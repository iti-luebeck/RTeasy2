/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.desert.core.Simulator;
import org.openide.awt.ActionReferences;

@ActionID(
        category = "Desert",
        id = "org.rteasy.actions.MicroStep"
)
@ActionRegistration(
        iconBase = "org/desert/core/images/microStep.png",
        displayName = "#CTL_MicroStep"
)
@ActionReferences({
    @ActionReference(path = "Toolbars/Desert", position = 600, name = "#CTL_MicroStep"),
    @ActionReference(path = "Menu/Simulate", position = 600, name = "#CTL_MicroStep")
})
public final class MicroStep implements ActionListener {

    
    public MicroStep(Simulator sim) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Simulator.microStep();
    }
}
