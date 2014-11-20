/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.helper;

import de.uniluebeck.iti.rteasy.kernel.Memory;
import de.uniluebeck.iti.rteasy.kernel.RegisterArray;
import java.util.Set;
import javax.swing.text.JTextComponent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Christian
 */
public class WindowHelper {

    /**
     * Searches for a TopComponent which is associated to the given object.
     * Returns null if no TopComponent is found.
     *
     * @param obj Object to which the TopComponent is associated
     * @return TopComponent or null if no one is found
     */
    public static TopComponent findTopComponent(Object obj) {
        Set<TopComponent> allOpened = WindowManager.getDefault().getRegistry().getOpened();
        for (TopComponent tC : allOpened) {
            if (tC.getLookup().lookup(Memory.class) == obj || tC.getLookup().lookup(RegisterArray.class) == obj) {
                return tC;
            }
        }
        return null;
    }

    /**
     * Get the last focused editor
     * @return JTextComponent
     */
    public static JTextComponent getLastFocusedEditor() {
        return org.netbeans.api.editor.EditorRegistry.lastFocusedComponent();
    }

}
