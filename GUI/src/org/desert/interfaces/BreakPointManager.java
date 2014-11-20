/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.interfaces;

/**
 *
 * @author Christian
 */
public interface BreakPointManager {
    public void removeBreakPoint(int state);
    public void highlightBreakPoint(int breakPoint);
    public void removeBreakPointHighlights();
}
