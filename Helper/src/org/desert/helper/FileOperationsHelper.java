/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.helper;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.openide.filesystems.FileChooserBuilder;

/**
 *
 * @author Christian
 */
public class FileOperationsHelper {

    public static File openFileDialog(FileFilter fF) {
        File workDir = new File(System.getProperty("user.home"));
        FileChooserBuilder fCB = new FileChooserBuilder("user-dir").setDefaultWorkingDirectory(workDir);
        fCB.setFileFilter(fF);
        fCB.setAcceptAllFileFilterUsed(false);
        return fCB.showOpenDialog();
    }
 
    public static File saveFileDialog(FileFilter fF) {
        File workDir = new File(System.getProperty("user.home"));
        FileChooserBuilder fCB = new FileChooserBuilder("user-dir").setDefaultWorkingDirectory(workDir);
        fCB.setFileFilter(fF);
        fCB.setAcceptAllFileFilterUsed(false);
        return fCB.showSaveDialog();
    }
    
    public static File openFileDialog() {
        File workDir = new File(System.getProperty("user.home"));
        FileChooserBuilder fCB = new FileChooserBuilder("user-dir").setDefaultWorkingDirectory(workDir);
        return fCB.showOpenDialog();
    }
    
}
