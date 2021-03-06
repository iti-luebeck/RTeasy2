/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.gui;

import java.util.Arrays;
import java.util.HashSet;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.desert.interfaces.BreakPointManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.desert.gui//BreakPointViewer//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "BreakPointViewerTopComponent",
        iconBase = "org/rteasy/images/Debug.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "right", openAtStartup = false)
public final class BreakPointViewerTopComponent extends TopComponent {

    private final HashSet<Integer> breakpoints;
    private final TableModel model;
    private BreakPointManager breakPointManager;

    public BreakPointViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(BreakPointViewerTopComponent.class, "CTL_BreakPointViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(BreakPointViewerTopComponent.class, "HINT_BreakPointViewerTopComponent"));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);

        breakpoints = new HashSet();
        model = new TableModel();
        breakPointTable.setModel(model);

        breakPointTable.setCellSelectionEnabled(true);
        breakPointTable.getSelectionModel().addListSelectionListener(new BreakPointListSelectionListener());
    }

    class BreakPointListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int row = breakPointTable.getSelectedRow();
                if (row >= 0) {
                    int breakPoint = ((Integer)model.breakpointsSorted[row]);
                    breakPointManager.highlightBreakPoint(breakPoint);
                    delBtn.setEnabled(true);
                } else {
                    delBtn.setEnabled(false);
                    breakPointManager.removeBreakPointHighlights();
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        breakPointTable = new javax.swing.JTable();
        delBtn = new javax.swing.JButton();
        deselButton = new javax.swing.JButton();

        breakPointTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        breakPointTable.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(breakPointTable);

        org.openide.awt.Mnemonics.setLocalizedText(delBtn, org.openide.util.NbBundle.getMessage(BreakPointViewerTopComponent.class, "BUTTON_DELETE")); // NOI18N
        delBtn.setEnabled(false);
        delBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delBtnActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(deselButton, org.openide.util.NbBundle.getMessage(BreakPointViewerTopComponent.class, "BUTTON_RELEASE")); // NOI18N
        deselButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deselButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(delBtn)
                .addGap(18, 18, 18)
                .addComponent(deselButton)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delBtn)
                    .addComponent(deselButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void delBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBtnActionPerformed
        int row = breakPointTable.getSelectedRow();
        if (row >= 0) {
            int breakPoint = ((Integer) model.breakpointsSorted[row]);
            breakPointManager.removeBreakPoint(breakPoint);
        }
    }//GEN-LAST:event_delBtnActionPerformed

    private void deselButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deselButtonActionPerformed
        breakPointTable.clearSelection();
        delBtn.setEnabled(false);
    }//GEN-LAST:event_deselButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable breakPointTable;
    private javax.swing.JButton delBtn;
    private javax.swing.JButton deselButton;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    public boolean isRegistered() {
        return breakPointManager != null;
    }

    public void registerBreakPointManager(BreakPointManager breakPointManager) {
        this.breakPointManager = breakPointManager;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public void addBreakPoint(int b) {
        breakpoints.add(b);
        model.fireTableDataChanged();
        int nOB = model.breakpointsSorted.length;
        int i;
        for (i = 0; ((Integer) model.breakpointsSorted[i]) != b && i < nOB; i++) {
        }
        breakPointTable.setColumnSelectionInterval(0, 0);
        if (i < nOB) {

            breakPointTable.setRowSelectionInterval(i, i);
        } else {
            breakPointTable.setRowSelectionInterval(0, 0);
        }
    }

    public void removeBreakPoint(int b) {
        breakpoints.remove(b);
        model.fireTableDataChanged();
    }

    class TableModel extends AbstractTableModel {

        public Object breakpointsSorted[];

        TableModel() {
            readBreakpoints();
        }

        private void readBreakpoints() {
            breakpointsSorted = breakpoints.toArray();
            Arrays.sort(breakpointsSorted);
        }

        @Override
        public void fireTableDataChanged() {
            readBreakpoints();
            super.fireTableDataChanged();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int col) {
            if (col == 0 && row < breakpointsSorted.length && row >= 0) {
                return NbBundle.getMessage(BreakPointViewerTopComponent.class, "LABLE_STATE") + " " + breakpointsSorted[row].toString();
            } else {
                return "";
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
        }

        @Override
        public String getColumnName(int col) {
            if (col == 0) {
                return NbBundle.getMessage(BreakPointViewerTopComponent.class, "LABEL_BREAKPOINTS") + ":";
            } else {
                return "";
            }
        }

        @Override
        public int getRowCount() {
            return breakpointsSorted.length;
        }

        @Override
        public int getColumnCount() {
            return 1;
        }
    }

}
