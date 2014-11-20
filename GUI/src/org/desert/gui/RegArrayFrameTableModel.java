package org.desert.gui;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.SimObjectsBase;
import de.uniluebeck.iti.rteasy.kernel.BitVector;
import de.uniluebeck.iti.rteasy.kernel.RegisterArray;
import org.openide.util.NbBundle;

public class RegArrayFrameTableModel extends AbstractTableModel {

    private final RegisterArray regarray;
    public SimObjectsBase[] regBase;
    private final int rowsUsed, base;
    private final Component parent;

    RegArrayFrameTableModel(RegisterArray r, Component tp) {
        base = RTSimGlobals.BASE_HEX;
        regarray = r;
        parent = tp;
        rowsUsed = r.getLength() + 1;
        regBase = new SimObjectsBase[r.getLength()];
        for (int i = 0; i < regBase.length; i++) {
            regBase[i] = new SimObjectsBase(RTSimGlobals.BASE_BIN);
        }
    }

    // TODO same as getRowCount
    public int getregwidth() {
        return regarray.getLength();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1 || col == 2;
    }

    @Override
    public int getRowCount() {
        return regarray.getLength();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    public boolean registerValueChangedAt(int row, int col) {
        if (col == 1) {
        }
        return true;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0 && row < rowsUsed) {
            return Integer.toString(row).toUpperCase();
        } else if (col == 1 && row < rowsUsed) {
            return regarray.getRegister(row).getContentStr(regBase[row].getValue());
        } else if (col == 2 && row < rowsUsed) {
            return regBase[row];
        } else {
            return "";
        }
    }

    // TODO Internationalisierung
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 1 && row < rowsUsed) {
            try {
                regarray.editContent(new BitVector(value.toString()), row);
                fireTableDataChanged();
            } catch (NumberFormatException e) {
                JOptionPane.showInternalMessageDialog(parent,
                        RTSimGlobals.baseInputErrorMsg(base),
                        "Eingabefehler in Speicherzelle", JOptionPane.ERROR_MESSAGE);
            }
        } else if (col == 2 && row < rowsUsed) {
            regBase[row] = (SimObjectsBase) value;
            fireTableDataChanged();
        }
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return NbBundle.getMessage(RegArrayFrameTableModel.class, "COLUMN_POSITION");
            case 1:
                return NbBundle.getMessage(RegArrayFrameTableModel.class, "BUTTON_CONTENT");
            case 2:
                return NbBundle.getMessage(RegArrayFrameTableModel.class, "LABEL_BASE");
        }
        return "";
    }
}
