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
        id = "org.desert.core.actions.Stop"
)
@ActionRegistration(
        iconBase = "org/desert/core/images/Stop.png",
        displayName = "#CTL_Stop"
)
@ActionReferences({
    @ActionReference(path = "Menu/Simulate", position = 300),
    @ActionReference(path = "Toolbars/Desert", position = 300)
})
public final class Stop implements ActionListener {

    private final Simulator.Runner context;

    public Stop(Simulator.Runner context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Simulator.stop();
    }
}
