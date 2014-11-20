/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.helper;

import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * This class is used to replace the RTLog of RTeasy. It uses the NetBeans
 * IOProvider to log messages and errors.
 *
 * @author Christian
 */
public class WrapperIOLog {

    private static final InputOutput io = IOProvider.getDefault().getIO("Log", false);

    /**
     * Writes a given message in the ide log.
     *
     * @param msg Message to display.
     */
    public static void logMsg(String msg) {
        io.getOut().println(msg);
        io.getOut().close();
    }

    /**
     * Writes a given message in the ide error log.
     *
     * @param err Error message to display.
     */
    public static void logErr(String err) {
        io.getErr().println(err);
        io.getErr().close();
    }
}
