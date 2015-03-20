/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core;

import de.uniluebeck.iti.rteasy.PositionRange;
import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.SignalsData;
import de.uniluebeck.iti.rteasy.frontend.ASTRtProg;
import de.uniluebeck.iti.rteasy.frontend.RTSim_Parser;
import de.uniluebeck.iti.rteasy.gui.RTOptions;
import de.uniluebeck.iti.rteasy.kernel.Expression;
import de.uniluebeck.iti.rteasy.kernel.RTProgram;
import de.uniluebeck.iti.rteasy.kernel.RTSim_SemAna;
import de.uniluebeck.iti.rteasy.kernel.RegisterArray;
import de.uniluebeck.iti.rteasy.kernel.Statement;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import org.desert.core.highlighters.StateHighlighter;
import org.desert.gui.BreakPointViewerTopComponent;
import org.desert.gui.SimStateTopComponent;
import org.desert.helper.CentralLookup;
import org.desert.helper.WindowHelper;
import org.desert.helper.WrapperIOLog;
import org.openide.cookies.SaveCookie;
import org.openide.util.Exceptions;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class Simulator {

    private static RTSim_SemAna semAna;
    private static ASTRtProg rn;
    private static RTSim_Parser parser;
    private static RTProgram rtprog;
    private static StateHighlighter sH;
    private static Runner runner;
    private static Thread runThread;
    private static Result openCookies;

    /**
     * Stores the current mode.
     *
     * Editing: 1, Simulating: 2
     */
    private static Modes mode = Modes.edit;

    public static void changeMode() {
        switch (Simulator.mode) {
            case edit:
                setSimulateMode();
                break;
            case simulate:
                cancel();
                break;
        }
    }

    private static void setSimulateMode() {
        SaveCookie sC = Utilities.actionsGlobalContext().lookup(SaveCookie.class);
        if (sC != null) {
            try {
                sC.save();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        if (Simulator.compile(WindowHelper.getLastFocusedEditor().getDocument())) {
            CentralLookup.getDefault().add(new Simulator());
            Simulator.mode = Modes.simulate;
            WindowHelper.getLastFocusedEditor().setEditable(false);
            openSimStateWindow(semAna.getRegisterOrder(), semAna.getBusOrder(), semAna.getMemoryOrder(), semAna.getRegArrayOrder());
            openBreakPointViewer();

            WrapperIOLog.logMsg(NbBundle.getMessage(Simulator.class, "MSG_COMPILE_SUCCESS"));
        }
    }

    private static void setEditMode() {
        Simulator.mode = Modes.edit;
        Mode m = WindowManager.getDefault().findMode("right");
        for (TopComponent tC : m.getTopComponents()) {
            tC.close();
        }
        m = WindowManager.getDefault().findMode("simstate");
        for (TopComponent tC : m.getTopComponents()) {
            tC.close();
        }
        m = WindowManager.getDefault().findMode("memviewer");
        for (TopComponent tC : m.getTopComponents()) {
            tC.close();
        }
        for (Simulator sim : CentralLookup.getDefault().lookupAll(Simulator.class)) {
            CentralLookup.getDefault().remove(sim);
        }
        sH.removeAllStepMarks();
        BreakPointManager.getDefault().removeAllBreakPoints();
        if (WindowHelper.getLastFocusedEditor() != null) {
            WindowHelper.getLastFocusedEditor().setEditable(true);
        }
    }

    public static Modes getMode() {
        return mode;
    }

    // TODO Rename to StateHighlighter
    public static void setStepHighlighter(StateHighlighter sH) {
        Simulator.sH = sH;
    }

    public static boolean compile(Document d) {
        try {
            parser = new RTSim_Parser(new StringReader(d.getText(0, d.getLength())));
            rn = parser.parseRTProgram();
            if (parser.hasSyntaxError()) {
                WrapperIOLog.logErr(parser.getSyntaxErrorMessage());
                return false;
            }
            semAna = new RTSim_SemAna();
            int errorCount = semAna.checkRTProgram(rn);
            if (errorCount > 0) {
                logErrors();
            }
            if (semAna.hasWarnings()) {
                logWarnings();
            }
            if (errorCount > 0) {
                return false;
            }
            rtprog = new RTProgram(rn, semAna);
            return true;
        } catch (BadLocationException bLE) {
            String msg = bLE.getLocalizedMessage();
            if (msg == null) {
                WrapperIOLog.logErr(NbBundle.getMessage(Simulator.class, "INTERNAL_ERROR_STACK_TRACE"));
                try {
                    for (StackTraceElement stackTrace : bLE.getStackTrace()) {
                        WrapperIOLog.logErr(stackTrace.toString());
                    }
                } catch (Throwable t) {
                    WrapperIOLog.logErr(NbBundle.getMessage(Simulator.class, "INTERNAL_ERROR_STACK_TRACE"));
                }
            } else {
                WrapperIOLog.logErr(msg);
            }
            return false;
        }
    }

    /**
     * Logs error messages from the semantic analysis.
     */
    private static void logErrors() {
        LinkedList errorMsg = semAna.getErrorMessages();
        LinkedList errorPos = semAna.getErrorPositions();
        ListIterator msgIt = errorMsg.listIterator(0);
        ListIterator posIt = errorPos.listIterator(0);
        while (msgIt.hasNext() && posIt.hasNext()) {
            WrapperIOLog.logErr(NbBundle.getMessage(Simulator.class, "TITLE_ERROR") + ": "
                    + ((PositionRange) posIt.next()).toString() + ": "
                    + (String) msgIt.next());
        }
    }

    /**
     * Logs warnings from the semantic analysis.
     */
    private static void logWarnings() {
        LinkedList warnMsg = semAna.getWarningMessages();
        LinkedList warnPos = semAna.getWarningPositions();
        ListIterator msgIt = warnMsg.listIterator(0);
        ListIterator posIt = warnPos.listIterator(0);
        while (msgIt.hasNext() && posIt.hasNext()) {
            WrapperIOLog.logMsg(NbBundle.getMessage(Simulator.class, "TITLE_WARNING") + ": "
                    + ((PositionRange) posIt.next()).toString() + ": "
                    + (String) msgIt.next());
        }
    }

    public static boolean step() {
        if (rtprog.terminated()) {
            logTermination();
            return false;
        }
        boolean bk = true;
        int smmod = 1;
        // TODO unsetMicroStepMark
        /*
         sL.unsetMicroStepMark();
         }*/
        if (BreakPointManager.getDefault().hasBreakPoint(rtprog.getStatSeqIndex())) {
            smmod = 2;
            bk = false;
        }
        PositionRange tpr = rtprog.getCurrentPositionRange();
        if (!rtprog.step()) {
            WrapperIOLog.logErr(rtprog.getErrorMessage());
        }
        if (rtprog.terminated()) {
            logTermination();
            bk = false;
        }
        setStepMark(tpr, smmod);
        Simulator.updateSimObjectsWindow();
        return bk;
    }

    public static void microStep() {
        if (rtprog.terminated()) {
            logTermination();
            return;
        }
        PositionRange tpr = rtprog.getCurrentPositionRange();
        if (!rtprog.microStep()) {
            WrapperIOLog.logErr(rtprog.getErrorMessage());
        }
        if (rtprog.terminated()) {
            logTermination();
        }
        Statement st = rtprog.getCurrentStatement();
        if (st != null) {
            tpr = st.getPositionRange();
        }
        setMicroStepMark(tpr, st.getStatementType(), st);
        updateSimObjectsWindow();
    }

    public static void reset() {
        rtprog.reset();
        updateSimObjectsWindow();
        sH.removeAllStepMarks();
    }

    public static void run() {
        runner = new Runner();
        runner.setRunPermission(true);
        runThread = new Thread(runner);
        EventQueue.invokeLater(runThread);
        CentralLookup.getDefault().add(runner);
        for (Simulator s : CentralLookup.getDefault().lookupAll(Simulator.class)) {
            CentralLookup.getDefault().remove(s);
        }
    }

    /**
     * Called by the stop action. Stops the simulation and adds simulator
     * instance to CentralLookup to enable simulation actions.
     */
    public static void stop() {
        if (runner != null) {
            runner.setRunPermission(false);
            // enable simulation actions
            CentralLookup.getDefault().add(new Simulator());
        }
    }

    /**
     * Called if tab or the application is closed and if mode changes during the
     * simulation. If simulation runs it will be canceled and mode is set to
     * edit mode.
     */
    public static void cancel() {
        setEditMode();
        if (runner != null) {
            runner.setRunPermission(false);
        }
    }

    private static void logTermination() {
        WrapperIOLog.logMsg(NbBundle.getMessage(Simulator.class, "MSG_SIMULATION_TERMINATED"));
    }

    private static void setStepMark(PositionRange pr, int mod) {
        Element root = WindowHelper.getLastFocusedEditor().getDocument().getDefaultRootElement();
        int stepMarkBegin = root.getElement(pr.beginLine - 1).getStartOffset()
                + pr.beginColumn - 1;
        int stepMarkEnd = root.getElement(pr.endLine - 1).getStartOffset()
                + pr.endColumn;
        Color col;
        if (mod == 1) {
            col = Color.CYAN;
        } else if (mod == 2) {
            col = Color.RED;
        } else {
            return;
        }
        sH.updateStepMark(stepMarkBegin, stepMarkEnd, col);
    }

    private static void setMicroStepMark(PositionRange pr, int statementType,
            Statement st) {
        Element root = WindowHelper.getLastFocusedEditor().getDocument().getDefaultRootElement();
        int begin = root.getElement(pr.beginLine - 1).getStartOffset()
                + pr.beginColumn - 1;
        int end = root.getElement(pr.endLine - 1).getStartOffset()
                + pr.endColumn;
        int begincase = 0;
        int endcase = 0;
        Color hcolor = Color.YELLOW;
        if (statementType == RTSimGlobals.IFBAILOUT
                || statementType == RTSimGlobals.SWITCHBAILOUT) {
            if (st.getIfExpr()) {
                hcolor = Color.GREEN;
            } else {
                hcolor = Color.RED;
            }
            if (statementType == RTSimGlobals.SWITCHBAILOUT) {
                hcolor = Color.yellow;
                PositionRange cpr = st.getCasePosition();
                begincase = root.getElement(cpr.beginLine - 1).getStartOffset()
                        + cpr.beginColumn - 1;
                endcase = root.getElement(cpr.endLine - 1).getStartOffset()
                        + cpr.endColumn;
            }
        }
        sH.updateStepMark(begin, end, hcolor);
        if (statementType == RTSimGlobals.SWITCHBAILOUT) {
            // TODO second Highlight begincase endcase hcolor
        }
    }

    public static String getSignals() {
        int max;
        ArrayList el;
        Expression e;
        Statement s;
        Iterator it;
        String output = "";
        RTOptions.calculateSignals = true;
        Document doc = WindowHelper.getLastFocusedEditor().getDocument();
        if (compile(doc)) {
            output = NbBundle.getMessage(Simulator.class, "CONDITION_SIGNALS")
                    + " (I) :\n----------------\n";
            SignalsData sigData = rtprog.getSignalsData();
            ArrayList inputSignals = sigData.getInputSignals();
            max = inputSignals.size();
            for (int i = 0; i < max; i++) {
                el = (ArrayList) inputSignals.get(i);
                it = el.listIterator();
                if (it.hasNext()) {
                    e = (Expression) it.next();
                    output += "I" + Integer.toString(i) + "\t" + e.toString() + "\n";
                }
            }
            output += "\n" + NbBundle.getMessage(Simulator.class, "CONTROL_SIGNALS")
                    + " (C) :\n----------------\n";
            ArrayList controlSignals = rtprog.getSignalsData()
                    .getControlSignals();
            max = controlSignals.size();
            for (int i = 0; i < max; i++) {
                el = (ArrayList) controlSignals.get(i);
                it = el.listIterator();
                if (it.hasNext()) {
                    s = (Statement) it.next();
                    output += "C" + Integer.toString(i) + "\t" + s.toString()
                            + "\n";
                }
            }
        }
        RTOptions.calculateSignals = false;
        return output;
    }

    public static String getVHDL(String kind) {
        RTOptions.calculateSignals = true;
        StringWriter sw = new StringWriter();
        Document doc = WindowHelper.getLastFocusedEditor().getDocument();
        if (compile(doc)) {
            Hashtable regArHash = rtprog.getRegArrays();
            String newdecl = "";
            if (!regArHash.isEmpty()) {
                for (Enumeration e = regArHash.elements(); e.hasMoreElements();) {
                    RegisterArray ra = (RegisterArray) e.nextElement();
                    newdecl = newdecl + ra.getSingleRegDecl();
                }

            }
            PrintWriter pw = new PrintWriter(sw);
            if (kind.equals("cu")) {
                rtprog.emitAllInOne(pw);
            } else if (kind.equals("ou")) {
                rtprog.emitAllInOne(pw);
            } else if (kind.equals("tb")) {
                rtprog.emitTestBenchFrame(pw);
            } else if (kind.equals("all")) {
                rtprog.emitAllInOne(pw);
            } else {
                pw.println("falscher Parameter fuer showVHDL()");
            }
            sw.flush();
        }
        RTOptions.calculateSignals = false;
        return sw.toString();
    }

    private static void openSimStateWindow(LinkedList registerOrder,
            LinkedList busOrder, LinkedList memoryOrder, LinkedList regArrOrder) {
        SimStateTopComponent simStateTopCmpnt = (SimStateTopComponent) WindowManager.getDefault().findTopComponent("SimStateTopComponent");

        if (simStateTopCmpnt == null) {
            simStateTopCmpnt = new SimStateTopComponent();
        }

        Mode m = WindowManager.getDefault().findMode("simstate");
        m.dockInto(simStateTopCmpnt);

        if (!simStateTopCmpnt.isOpened()) {
            simStateTopCmpnt.open();
        }

        simStateTopCmpnt.setData(registerOrder, busOrder, memoryOrder, regArrOrder);
        simStateTopCmpnt.initTable();
    }

    private static void openBreakPointViewer() {
        TopComponent tC = WindowManager.getDefault().findTopComponent("BreakPointViewerTopComponent");
        if (tC == null) {
            tC = new BreakPointViewerTopComponent();
        }
        Mode m = WindowManager.getDefault().findMode("right");
        m.dockInto(tC);
        if (!((BreakPointViewerTopComponent) tC).isRegistered()) {
            BreakPointManager.getDefault().registerBreakPointViewer((BreakPointViewerTopComponent) tC);
        }
        if (!tC.isOpened()) {
            tC.open();
        }
    }

    private static void updateSimObjectsWindow() {
        SimStateTopComponent simObjTopCmpnt = (SimStateTopComponent) WindowManager.getDefault().findTopComponent("SimStateTopComponent");
        if (!simObjTopCmpnt.isOpened()) {
            simObjTopCmpnt.open();
        }
        simObjTopCmpnt.setStateCounter(rtprog.getStatSeqIndex());
        simObjTopCmpnt.setCycleCount(rtprog.getCycleCount());
        simObjTopCmpnt.simUpdate();
    }

    public static RTProgram getRTProg() {
        return rtprog;
    }

    static class OpenCookieListener implements LookupListener {

        @Override
        public void resultChanged(LookupEvent ev) {
            if (openCookies.allInstances().isEmpty()) {
                setEditMode();
            }
        }

    }

    public static class Runner implements Runnable {

        private boolean runPermission = false;

        Runner() {
        }

        public synchronized void setRunPermission(boolean p) {
            runPermission = p;
        }

        public synchronized boolean checkRunPermission() {
            return runPermission;
        }

        @Override
        public void run() {
            if (checkRunPermission() && step()) {
                EventQueue.invokeLater(this);
            } else {
                for (Runner r : CentralLookup.getDefault().lookupAll(Runner.class)) {
                    CentralLookup.getDefault().remove(r);
                }
            }
        }
    }
}
