/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Christian
 */
public class RTFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            String[] s = f.getName().split("\\.");
            return s.length > 0 && s[s.length - 1].equals("rt");
        }
    }

    @Override
    public String getDescription() {
        // TODO Sprachabhängig machen
        return "RT Datei";
    }

}
