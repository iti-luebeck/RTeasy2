/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.core;

import de.uniluebeck.iti.rteasy.PositionRange;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import org.desert.gui.BreakPointViewerTopComponent;
import org.desert.helper.WindowHelper;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.text.Annotation;
import org.openide.text.Line;
import org.desert.core.annotations.BreakPointAnnotation;
import org.desert.core.highlighters.BreakPointHighlighter;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Utilities;

public class BreakPointManager implements org.desert.interfaces.BreakPointManager {

    private static BreakPointManager bPM;

    public static BreakPointManager getDefault() {
        if (bPM == null) {
            bPM = new BreakPointManager();
        }
        return bPM;
    }

    private BreakPointViewerTopComponent breakPointViewer;
    private final HashSet<Integer> breakpoints;
    private final HashMap<Integer, Annotation> annotations;
    private BreakPointHighlighter breakPointHighlighter;

    public BreakPointManager() {
        annotations = new HashMap<>();
        breakpoints = new HashSet<>();
    }

    /**
     * Called by BreakPointHighlighter class to set reference of the
     * highlighter.
     *
     * @param breakPointHighlighter
     */
    public void setBreakPointHighlighter(BreakPointHighlighter breakPointHighlighter) {
        this.breakPointHighlighter = breakPointHighlighter;
    }

    /**
     * Adds annotation for the given state.
     *
     * @param state Index of the state
     */
    private void addAnnotation(Integer state) {
        PositionRange pR = getPositionRangeOfState(state);
        int pos = getLineStartOffset(pR.beginLine - 1) + pR.beginColumn;
        JTextComponent ed = WindowHelper.getLastFocusedEditor();
        Document doc = ed.getDocument();
        //int pos = ed.getCaret().getDot();
        Line l = NbEditorUtilities.getLine(doc, pos, false);
        Annotation a = new BreakPointAnnotation();
        a.attach(l);
        annotations.put(state, a);
    }

    /**
     * Remove annotation for given state
     *
     * @param state Index of the state
     */
    private void removeAnnotation(Integer state) {
        annotations.get(state).detach();
        annotations.remove(state);
    }

    /**
     * Finds the first occurrence of characters in given String object.
     *
     * @param line String to find first position of character
     * @return Number of the first position or zero if no characters are found
     */
    private int detectFirstCharPositionInLine(String line) {
        Matcher m = Pattern.compile("([a-z,A-Z])").matcher(line);
        int pos = 0;
        if (m.find()) {
            pos = m.start();
        }
        return pos;
    }

    /**
     * Finds the state which is at current caret position.
     *
     * @return state number or -1 if no state was found
     */
    private int getStateAtCaretPosition() {
        Document doc = WindowHelper.getLastFocusedEditor().getDocument();
        int dot = WindowHelper.getLastFocusedEditor().getCaret().getDot();
        Line l = NbEditorUtilities.getLine(doc, dot, false);
        int lNr = l.getLineNumber();
        int col = 0;
        try {
            col = Utilities.getVisualColumn((BaseDocument) doc, dot);
        } catch (BadLocationException e) {

        }
        if (col == 0) {
        dot += detectFirstCharPositionInLine(l.getText());
        }
        int column = dot - getLineStartOffset(lNr) + 1;
        lNr++;
        return getState(lNr, column);
    }

    private int getLineStartOffset(int lineNumber) {
        return WindowHelper.getLastFocusedEditor().getDocument().getDefaultRootElement().getElement(lineNumber).getStartOffset();
    }

    /**
     * This method is called from the actions. It searches the state at the
     * current caret position and toggles an breakpoint for it.
     */
    public void toggleBreakPoint() {
        int state = getStateAtCaretPosition();
        if (state != -1) {
            if (breakpoints.contains(state)) {
                removeBreakPoint(state);
            } else {
                addBreakPoint(state);
            }
        }
    }

    public void addBreakPoint(int state) {
        if (breakPointViewer != null) {
            breakPointViewer.addBreakPoint(state);
        }
        addAnnotation(state);
        breakpoints.add(state);
    }

    public void registerBreakPointViewer(BreakPointViewerTopComponent breakPointViewer) {
        this.breakPointViewer = breakPointViewer;
        breakPointViewer.registerBreakPointManager(this);
    }

    public void removeAllBreakPoints() {
        Integer[] breakPointArray = {};
        breakPointArray = breakpoints.toArray(breakPointArray);
        for(int breakPoint:breakPointArray) {
            removeBreakPoint(breakPoint);
        }
    }
    
    @Override
    public void removeBreakPoint(int state) {
        if (breakPointViewer != null) {
            breakPointViewer.removeBreakPoint(state);
        }
        removeAnnotation(state);
        breakpoints.remove(state);
    }

    public boolean hasBreakPoint(int state) {
        return breakpoints.contains(state);
    }

    /**
     * Returns number of selected state in editor.
     *
     * @param line Linenumber
     * @param col Column number
     * @return Number of the state or -1 if no state is selected
     */
    private int getState(int line, int col) {
        return Simulator.getRTProg().getParStatsIndexAtPosition(line, col);
    }

    public void removeBreakPointViewer(BreakPointViewerTopComponent bV) {
        breakPointViewer.remove(bV);
    }

    /**
     * Return the position range of given state.
     *
     * @param breakPoint Index of the state.
     * @return PositionRange
     */
    private PositionRange getPositionRangeOfState(int breakPoint) {
        return Simulator.getRTProg().getPositionRangeAt(breakPoint, 0);
    }

    @Override
    public void highlightBreakPoint(int breakPoint) {
        PositionRange pr = getPositionRangeOfState(breakPoint);
        Element root = WindowHelper.getLastFocusedEditor().getDocument().getDefaultRootElement();
        int begin = root.getElement(pr.beginLine - 1).getStartOffset()
                + pr.beginColumn - 1;
        int end = root.getElement(pr.endLine - 1).getStartOffset()
                + pr.endColumn;
        Color red = new Color(Integer.decode("#FC9D9F"));
        breakPointHighlighter.setMark(begin, end, red);
    }

    @Override
    public void removeBreakPointHighlights() {
        breakPointHighlighter.removeAllMarks();
    }

}
