/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.desert.core.BreakPointManager;
import org.desert.core.Simulator;
import org.desert.helper.WindowHelper;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Utilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;

@ActionRegistration(
        displayName = "#CTL_ToggleBreakPoint"
)
@ActionID(category = "Desert", id = "org.desert.core.actions.ToggleBreakPointGlyphGutter")
@ActionReference(path = "Editors/text/x-rt/GlyphGutterActions")
public class ToggleBreakPointGlyphGutter implements ActionListener {

    public ToggleBreakPointGlyphGutter(Simulator sim) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Document doc = WindowHelper.getLastFocusedEditor().getDocument();
            int dot = WindowHelper.getLastFocusedEditor().getCaret().getDot();
            int col = Utilities.getVisualColumn((BaseDocument) doc, dot);
            WindowHelper.getLastFocusedEditor().setCaretPosition(dot-col);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        BreakPointManager.getDefault().toggleBreakPoint();
    }

}
