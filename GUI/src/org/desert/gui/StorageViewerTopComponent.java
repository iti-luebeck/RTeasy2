/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.gui;

import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.SimObjectsBase;
import de.uniluebeck.iti.rteasy.frontend.StorageParser;
import de.uniluebeck.iti.rteasy.kernel.Storage;
import de.uniluebeck.iti.rteasy.kernel.StorageEntry;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import org.desert.helper.FileOperationsHelper;
import org.desert.helper.WrapperIOLog;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
        preferredID = "StorageViewerTopComponent",
        //TODO set Icon
        //iconBase = "org/rteasy/Datasource.gif",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "storviewer", openAtStartup = false)
public final class StorageViewerTopComponent extends TopComponent {

    private Storage s;
    private StorageFrameTableModel model;
    private String lf;

    public Storage getStorage() {
        return s;
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    public StorageViewerTopComponent() {
        //generated source
        initComponents();
        setToolTipText(NbBundle.getMessage(StorageViewerTopComponent.class, "HINT_StorageViewerTopComponent"));
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
    }

    public void initStorViewer(Storage s) {
        this.s = s;
        associateLookup(Lookups.singleton(s));
        setName(NbBundle.getMessage(StorageViewerTopComponent.class, "CTL_StorageViewerTopComponent") + ": " + s.getIdStr());

        model = new StorageFrameTableModel(s, this);
        table.setModel(model);
        PointerCellRenderer renderer = new PointerCellRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(renderer);
        renderer.setHorizontalAlignment(JTextField.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);
        final JTextField textField = new JTextField();
        textField.setHorizontalAlignment(JTextField.RIGHT);
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(textField));
        Dimension d = table.getPreferredSize();
        d.height = 250;
        table.setPreferredScrollableViewportSize(d);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        model.fireTableDataChanged();
        baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_BIN));
        baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_DEC));
        SimObjectsBase hexBase = new SimObjectsBase(RTSimGlobals.BASE_HEX);
        baseBox.addItem(hexBase);
        baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_DEC2));
        baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_HEX2));
        baseBox.setSelectedItem(hexBase);
        setLineFeed();
    }

    /**
     * This method determine the system linefeed and saves it to lf.
     */
    private void setLineFeed() {
        try {
            lf = System.getProperty("line.separator");
        } catch (Throwable t) {
            lf = "\n";
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        baseLbl = new javax.swing.JLabel();
        baseBox = new javax.swing.JComboBox();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        resetButton = new javax.swing.JButton();
        gotoButton = new javax.swing.JButton();
        predecessorBtn = new javax.swing.JButton();
        pageLbl = new javax.swing.JLabel();
        successorBtn = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(baseLbl, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "LABEL_BASE")); // NOI18N

        baseBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseBoxActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(loadButton, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "BUTTON_LOAD")); // NOI18N
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(saveButton, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "BUTTON_SAVE")); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        scrollPane.setViewportView(table);

        org.openide.awt.Mnemonics.setLocalizedText(resetButton, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "BUTTON_RESET")); // NOI18N
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(gotoButton, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "BUTTON_GOTO_ADDRESS")); // NOI18N
        gotoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(predecessorBtn, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "BUTTON_PREDECESSOR")); // NOI18N
        predecessorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                predecessorBtnActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(pageLbl, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "LABEL_PAGE")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(successorBtn, org.openide.util.NbBundle.getMessage(StorageViewerTopComponent.class, "BUTTON_SUCCESSOR")); // NOI18N
        successorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                successorBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loadButton)
                        .addGap(18, 18, 18)
                        .addComponent(saveButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(baseLbl)
                        .addGap(18, 18, 18)
                        .addComponent(baseBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(gotoButton)
                        .addGap(18, 18, 18)
                        .addComponent(resetButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(predecessorBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pageLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(successorBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(baseLbl)
                    .addComponent(baseBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gotoButton)
                    .addComponent(resetButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(predecessorBtn)
                    .addComponent(pageLbl)
                    .addComponent(successorBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(loadButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void baseBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baseBoxActionPerformed
        model.base = ((SimObjectsBase) baseBox.getSelectedItem()).getValue();
        model.fireTableDataChanged();
    }//GEN-LAST:event_baseBoxActionPerformed

    public void simUpdate() {
        model.fireTableDataChanged();
    }

    private StorageEntry parseStorageFile(File f) {
        try {
            FileReader fr;
            fr = new FileReader(f);
            StorageParser parser = new StorageParser(fr);
            parser.setAddrWidth(s.getMaxAddrWidth());
            parser.setDataWidth(s.getWidth());
            StorageEntry sE = parser.parse();
            fr.close();
            //TODO Internationalization
            if (parser.hasSyntaxError()) {
                WrapperIOLog.logErr("Syntax-Fehler bei Laden des Speicherinhalts\n" + parser.getSyntaxErrorMessage());
                return null;
            }
            return sE;
        } catch (IOException ioE) {
            WrapperIOLog.logErr("Eingabe-Fehler bei Laden des Speicherinhalts\n" + ioE.getLocalizedMessage());
            return null;
        }
    }

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        File file = FileOperationsHelper.openFileDialog(new FileNameExtensionFilter("RTeasy2 storage dump - r2sd", "r2sd"));

        if (file == null) {
            return;
        }

        StorageEntry sE = parseStorageFile(file);
        if (sE != null) {
            updateStorage(sE);
        }
    }//GEN-LAST:event_loadButtonActionPerformed

    private void updateStorage(StorageEntry sE) {
        s.clear();
        ListIterator it;
        boolean addr[];
        boolean data[];
        do {
            it = sE.entries.listIterator(0);
            addr = sE.addr;
            while (it.hasNext()) {
                data = (boolean[]) it.next();
                s.setDataAt(addr, data);
                RTSimGlobals.boolArrayInc(addr);
            }
            sE = sE.child;
        } while (sE != null);
        simUpdate();
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            File f = FileOperationsHelper.saveFileDialog(new FileNameExtensionFilter("RTeasy2 storage dump - r2sd", "r2sd"));
            if(f == null) {
                return;
            }
            if(!Pattern.matches(".+\\.r2sd", f.getName())){
                f = new File(f.getAbsolutePath()+".r2sd");
            }
            
            FileWriter fw;
            fw = new FileWriter(f);

            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());
            fw.write("# " + NbBundle.getMessage(StorageViewerTopComponent.class, "TEXT_GENERATED_BY_DESERT") + " " + df.format(new Date()) + lf);
            fw.write("# " + NbBundle.getMessage(StorageViewerTopComponent.class, "TEXT_STORAGE_CONFIG").replaceAll("%%MEMORY", s.getPrettyDecl()) + lf);
            fw.write("# " + NbBundle.getMessage(StorageViewerTopComponent.class, "TEXT_ADDRWIDTH") + ": " + Integer.toString(s.getMaxAddrWidth()) + ", " + NbBundle.getMessage(StorageViewerTopComponent.class, "TEXT_DATAWIDTH") + ": " + Integer.toString(s.getWidth()) + lf + lf);
            ArrayList cells = s.getUsedCellsSorted();
            boolean old_addr[] = null;
            boolean addr[];
            int aw = s.getMaxAddrWidth();
            int dw = s.getWidth();
            ListIterator it = cells.listIterator(0);
            int state = 0;
            while (it.hasNext()) {
                addr = (boolean[]) it.next();
                switch (state) {
                    case 1:
                        RTSimGlobals.boolArrayInc(old_addr);
                        if (RTSimGlobals.boolArrayCompare(addr, old_addr) == 0) {
                            fw.write("," + lf + "  " + RTSimGlobals.base2String(model.base) + RTSimGlobals.boolArray2String(s.getDataAt(addr), model.base));
                            break;
                        }
                        fw.write(";" + lf);
                    case 0:
                        fw.write("$" + RTSimGlobals.boolArray2String(addr, RTSimGlobals.BASE_HEX) + ":" + lf + "  " + RTSimGlobals.base2String(model.base)
                                + RTSimGlobals.boolArray2String(s.getDataAt(addr), model.base));
                        state = 1;
                        break;
                }
                old_addr = addr;
            }
            if (old_addr != null) {
                fw.write(";" + lf);
            } else {
                fw.write("$0: $0;" + lf);
            }
            fw.close();
        } catch (IOException iOE) {
            JOptionPane.showInternalMessageDialog(this, NbBundle.getMessage(StorageViewerTopComponent.class, "DIALOG_ERROR_MEMORY_SAVE") + ": " + iOE.getLocalizedMessage(), NbBundle.getMessage(StorageViewerTopComponent.class, "TITLE_ERROR"), JOptionPane.ERROR_MESSAGE);
            iOE.printStackTrace(System.err);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        this.s.clear();
        model.fireTableDataChanged();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void gotoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoButtonActionPerformed
        String bk = JOptionPane.showInputDialog(this, NbBundle.getMessage(StorageViewerTopComponent.class, "DIALOG_GOTO_ADDRESS_INPUT"), NbBundle.getMessage(StorageViewerTopComponent.class, "BUTTON_GOTO_ADDRESS"), JOptionPane.QUESTION_MESSAGE);
        try {
            BigInteger inputBI = new BigInteger(bk, 16);
            if (inputBI.compareTo(BigInteger.ZERO) < 0 || !model.addressValid(inputBI)) {
                JOptionPane.showMessageDialog(this,
                        NbBundle.getMessage(StorageViewerTopComponent.class, "DIALOG_GOTO_ADDRESS_OUT_OF_RANGE").replaceAll(
                                "%%RANGE", "0 .. " + model.getStorSize().toString(16).toUpperCase()),
                        NbBundle.getMessage(StorageViewerTopComponent.class, "TITLE_ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            BigInteger pageBI = new BigDecimal(inputBI).divide(new BigDecimal("" + model.getPageSize())).toBigInteger();
            model.setPage(pageBI.add(BigInteger.ONE));
            int pixels = 0;
            if (inputBI.compareTo(BigInteger.ZERO) >= 0) {
                int rows = inputBI.subtract(pageBI.multiply(new BigInteger("" + model.getPageSize()))).intValue();
                pixels = table.getRowHeight() * (rows - 1);
            }
            scrollPane.getVerticalScrollBar().setValue(pixels);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    NbBundle.getMessage(StorageViewerTopComponent.class, "DIALOG_HEX_INPUT_SYNTAX_ERROR").replaceAll(
                            "%%INPUT", "address"), NbBundle.getMessage(StorageViewerTopComponent.class, "TITLE_ERROR"), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_gotoButtonActionPerformed

    private void predecessorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_predecessorBtnActionPerformed
        model.changePage(false);
    }//GEN-LAST:event_predecessorBtnActionPerformed

    private void successorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_successorBtnActionPerformed
        model.changePage(true);
    }//GEN-LAST:event_successorBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox baseBox;
    private javax.swing.JLabel baseLbl;
    private javax.swing.JButton gotoButton;
    private javax.swing.JButton loadButton;
    private javax.swing.JLabel pageLbl;
    private javax.swing.JButton predecessorBtn;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JButton successorBtn;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    public String getPointer() {
        return RTSimGlobals.boolArray2String(s.getAddr(),
                RTSimGlobals.BASE_DEC);
    }

    public void enablePredecessorBtn(boolean b) {
        predecessorBtn.setEnabled(b);
    }

    public void enableSuccessorBtn(boolean b) {
        successorBtn.setEnabled(b);
    }

    public void setPageLabel(String s) {
        pageLbl.setText(s);
    }

    class PointerCellRenderer extends DefaultTableCellRenderer {

        public PointerCellRenderer() {
            super();
            setBackground(Color.WHITE);
            setForeground(Color.black);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int col) {
            setBackground(null);
            setHorizontalAlignment(0);
            super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, col);
            if (col == 0) {
                setHorizontalAlignment(JTextField.CENTER);
            } else {
                setHorizontalAlignment(JTextField.RIGHT);
            }
            int pageSize = ((StorageFrameTableModel) table.getModel()).getPageSize();
            BigInteger curPage = ((StorageFrameTableModel) table.getModel()).getCurPage().subtract(BigInteger.ONE);
            BigInteger curRow = curPage.multiply(new BigInteger("" + pageSize)).add(new BigInteger("" + row));
            BigInteger curAddr = new BigInteger(getPointer());
            if (curRow.compareTo(curAddr) == 0) {
                setBackground(Color.GREEN);
                setForeground(Color.BLACK);
            } else {
                setBackground(Color.white);
                setForeground(Color.black);
            }
            return this;
        }

    }
}