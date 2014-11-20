package org.desert.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.desert.core.AllClosedCookie;
import org.desert.core.RTFileFilter;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

@ActionID(category = "File", id = "org.desert.core.actions.OpenRTFile")
@ActionRegistration(displayName = "#CTL_OpenFile", iconBase = "org/desert/core/images/OpenFile.png", iconInMenu = true)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 10),
    @ActionReference(path = "Toolbars/File", position = 10)
})
public final class OpenRTFile implements ActionListener {

    
    public OpenRTFile(AllClosedCookie openCookie) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //The default dir to use if no value is stored
        File home = new File(System.getProperty("user.home"));

        //Now build a file chooser and invoke the dialog in one line of code
        //"user-dir" is our unique key
        FileChooserBuilder fCB = new FileChooserBuilder("user-dir").setDefaultWorkingDirectory(home);
        fCB.setFileFilter(new RTFileFilter());
        fCB.setAcceptAllFileFilterUsed(false);
        File toAdd = fCB.showOpenDialog();

        //Result will be null if the user clicked cancel or closed the dialog w/o OK
        if (toAdd != null) {
            try {
                DataObject.find(FileUtil.toFileObject(toAdd)).
                        getLookup().lookup(OpenCookie.class).open();
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}
