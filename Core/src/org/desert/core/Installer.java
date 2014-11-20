package org.desert.core;

import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.gui.IUI;
import de.uniluebeck.iti.rteasy.gui.RTOptions;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import org.desert.helper.CentralLookup;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.awt.ToolbarPool;
import org.openide.cookies.OpenCookie;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {

    private static void removeAllPopupListeners(Component c) {
        if (c instanceof Container) {
            for (Component c2 : ((Container) c).getComponents()) {
                for (MouseListener l : c2.getMouseListeners()) {
                    if (l.getClass().getName().contains("PopupListener")) {
                        c2.removeMouseListener(l);
                    }
                }
            }
        }
    }

    private Result<OpenCookie> result;

    @Override
    public void restored() {
        System.setProperty("netbeans.buildnumber", "1.0");
        removeToolbarPopupMenu();
        setOpenFileActionCookies();
        RTSimGlobals.init();
        Locale.setDefault(IUI.getLocale());
        IUI.init(RTOptions.locale);
        RTOptions.noisyWarnings = true;
    }

    private void setOpenFileActionCookies() {
        result = Utilities.actionsGlobalContext().lookupResult(OpenCookie.class);
        result.addLookupListener(new OpenCookieListener());
        EditorRegistry.addPropertyChangeListener(new EditorRegistryPropertyChangeListener());
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

            @Override
            public void run() {
                TopComponent output = WindowManager.getDefault().findTopComponent("output");
                if (output != null) {
                    output.putClientProperty("netbeans.winsys.tc.closing_disabled", Boolean.TRUE);
                    output.close();
                    output.open();
                }
            }
            
        });
    }

    private void removeToolbarPopupMenu() {
        WindowManager.getDefault().invokeWhenUIReady(
                new Runnable() {
                    @Override
                    public void run() {
                        removeAllPopupListeners(ToolbarPool.getDefault());
                    }
                });
    }

    private void removeAllClosedCookie() {
        for (AllClosedCookie aCC : CentralLookup.getDefault().lookupAll(AllClosedCookie.class)) {
            CentralLookup.getDefault().remove(aCC);
        }
    }

    /**
     * Listen for closing of an editor window. Clears openLookup if editor is
     * closed.
     */
    class EditorRegistryPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(EditorRegistry.LAST_FOCUSED_REMOVED_PROPERTY)) {
                CentralLookup.getDefault().add(new AllClosedCookie());
            }
        }

    }

    class OpenCookieListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent ev) {
            if (!result.allInstances().isEmpty()) {
                removeAllClosedCookie();
            }
        }
    }

}
