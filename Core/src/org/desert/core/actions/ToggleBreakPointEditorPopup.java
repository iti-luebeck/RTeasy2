/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.desert.core.BreakPointManager;
import org.desert.core.Simulator;
import org.desert.helper.CentralLookup;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup.Result;
import org.openide.util.actions.NodeAction;

@ActionRegistration(
        displayName = "#CTL_ToggleBreakPoint"
)
@ActionID(category = "Desert", id = "org.desert.core.actions.ToggleBreakPointEditorPopup")
@ActionReference(path = "Editors/text/x-rt/Popup")
public class ToggleBreakPointEditorPopup extends NodeAction implements ActionListener {
    
    private final Result<Simulator> result;
    
    public ToggleBreakPointEditorPopup() {
        result = CentralLookup.getDefault().lookupResult(Simulator.class);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        BreakPointManager.getDefault().toggleBreakPoint();
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        BreakPointManager.getDefault().toggleBreakPoint();
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return result.allInstances().size() > 0;
    }

    @Override
    public String getName() {
        // TODO Internationalisieren
        return "Toggle BreakPoint";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

}
