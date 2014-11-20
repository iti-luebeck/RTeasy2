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
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

@ActionID(
        category = "Desert",
        id = "org.desert.core.actions.Mode"
)
@ActionRegistration(
        iconBase = "org/desert/core/images/mode.png",
        displayName = "#CTL_Mode"
)
@ActionReferences({
    @ActionReference(path = "Toolbars/Desert", position = 100, name = "#CTL_Mode"),
    @ActionReference(path = "Menu/Simulate", position = 100, name = "#CTL_Mode")
})
public class Mode implements ActionListener {


    public Mode(Open oC) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Simulator.changeMode();
    }

}
