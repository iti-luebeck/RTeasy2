/*
 * Copyright (c) 2003-2013, University of Luebeck, Institute of Computer Engineering
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Luebeck, the Institute of Computer
 *       Engineering nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior
 *       written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE UNIVERSITY OF LUEBECK OR THE INSTITUTE OF COMPUTER
 * ENGINEERING BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package org.desert.gui;

import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.kernel.Memory;
import javax.swing.table.AbstractTableModel;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.openide.util.NbBundle;

public class MemoryFrameTableModel extends AbstractTableModel {

    private final Memory m;
    public int base = RTSimGlobals.BASE_HEX, memdw, memaw;
    private final boolean taddr[];
    private final Component parent;
    private final int pageSize = 1000000;
    private final BigInteger pageSizeBI = new BigInteger("" + pageSize);
    private BigInteger offset, curPage;
    private final BigInteger memSize, lastPage;

    MemoryFrameTableModel(Memory tm, Component tp) {
        m = tm;
        parent = tp;
        memaw = m.getAddrWidth();
        taddr = new boolean[memaw];
        memdw = m.getDataWidth();
        memSize = new BigInteger("2").pow(memaw);
        curPage = BigInteger.ONE;
        offset = BigInteger.ZERO;
        BigDecimal memSizeBD = new BigDecimal(memSize).divide(new BigDecimal("" + pageSize)).setScale(0, RoundingMode.UP);
        lastPage = memSizeBD.toBigInteger();
        setPageLabel();
        updateButtons();
    }

    private void updateButtons() {
        ((MemoryViewerTopComponent) parent).enablePredecessorBtn((curPage.compareTo(BigInteger.ONE)>0));
        ((MemoryViewerTopComponent) parent).enableSuccessorBtn((lastPage.compareTo(new BigInteger(""+curPage))>0));
    }

    public int getPageSize() {
        return pageSize;
    }

    public BigInteger getMemSize() {
        return memSize;
    }

    public BigInteger getOffset() {
        return offset;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1;
    }

    public BigInteger getCurPage() {
        return curPage;
    }

    @Override
    public int getRowCount() {
        int rowsLastPage = memSize.subtract(curPage.subtract(BigInteger.ONE).multiply(new BigInteger(""+pageSize))).intValue();
        return curPage!=lastPage?pageSize:rowsLastPage;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            BigInteger bI = new BigInteger("" + row).add(offset);
            return bI.toString(16).toUpperCase();
        } else if (col == 1) {
            String addrStr = new BigInteger("" + row).add(offset).toString(2);
            int i = 0;
            for (; i < addrStr.length(); i++) {
                taddr[i] = addrStr.charAt(addrStr.length() - i - 1)=='1'?true:false;
            }
            for(;i<taddr.length;i++) taddr[i]=false;
            
            //RTSimGlobals.intInBoolArray(taddr, row);
            return RTSimGlobals.boolArray2String(m.getDataAt(taddr), base);
        } else {
            return "";
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        BigInteger addrBI = offset.add(new BigInteger("" + row));
        if (col == 1 && addressValid(addrBI)) {
            String addrStr = addrBI.toString(2);
            int i = 0;
            for (; i < addrStr.length(); i++) {
                taddr[i] = addrStr.charAt(addrStr.length() - i - 1)=='1';
            }
            for(;i<taddr.length;i++) taddr[i]=false;
            try {
                m.setDataAt(taddr, RTSimGlobals.string2boolArray(value.toString(),
                        memdw, base));
            } catch (NumberFormatException e) {
                JOptionPane.showInternalMessageDialog(parent,
                        RTSimGlobals.baseInputErrorMsg(base),
                        "Eingabefehler in Speicherzelle", JOptionPane.ERROR_MESSAGE);
            }
            // TODO Internatinalisierung
        }
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(MemoryFrameTableModel.class, "COLUMN_ADDRESS");
            case 1:
                return NbBundle.getMessage(MemoryFrameTableModel.class, "BUTTON_CONTENT");
        }
        return "";
    }

    private void setPageLabel() {
        ((MemoryViewerTopComponent) parent).setPageLabel("" + curPage + "/" + lastPage);
    }

    private void updateOffset() {
        offset = curPage.subtract(BigInteger.ONE).multiply(pageSizeBI);
    }

    public boolean addressValid(BigInteger addr) {
        return (addr.compareTo(new BigInteger("0")) >= 0 && addr.compareTo(memSize) <= 0);
    }

    /**
     *
     * @param next true if next false if previous page
     */
    public void changePage(boolean next) {
        curPage = next ? curPage.add(BigInteger.ONE) : curPage.subtract(BigInteger.ONE);
        setPageLabel();
        updateOffset();
        updateButtons();
        fireTableDataChanged();
    }

    public void setPage(BigInteger page) {
        if (page.compareTo(BigInteger.ONE) >= 0 && page.compareTo(lastPage) <= 0) {
            curPage = page;
            setPageLabel();
            updateOffset();
            updateButtons();
            fireTableDataChanged();
        }
    }
}
