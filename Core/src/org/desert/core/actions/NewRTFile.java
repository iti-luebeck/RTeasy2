/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import org.desert.core.AllClosedCookie;
import org.desert.core.RTFileFilter;
import org.desert.helper.FileOperationsHelper;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.desert.core.actions.NewRTFile"
)
@ActionRegistration(
        iconBase = "org/desert/core/images/NewFile.png",
        displayName = "#CTL_NewRTFile"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 5),
    @ActionReference(path = "Toolbars/File", position = 5)
})
@Messages("CTL_NewRTFile=New RT File")
public final class NewRTFile implements ActionListener {


    public NewRTFile(AllClosedCookie context) {
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        RTFileFilter rtFF = new RTFileFilter();
        File toAdd = FileOperationsHelper.saveFileDialog(rtFF);
        if (toAdd != null) {
            if (!rtFF.accept(toAdd)) {
                toAdd = new File(toAdd.getAbsolutePath() + ".rt");
            }
            try {
                FileObject fO = FileUtil.createData(toAdd);
                DataObject.find(fO).getLookup().lookup(OpenCookie.class).open();
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
