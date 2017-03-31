/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.desert.gui;

import de.uniluebeck.iti.rteasy.RTSimGlobals;
import de.uniluebeck.iti.rteasy.SimObjectsBase;
import de.uniluebeck.iti.rteasy.kernel.Memory;
import de.uniluebeck.iti.rteasy.kernel.RegisterArray;
import de.uniluebeck.iti.rteasy.kernel.Storage;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import org.desert.*;
import org.desert.helper.WindowHelper;
import org.desert.helper.WrapperIOLog;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.NbBundle;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.desert.gui//SimObjects//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "SimStateTopComponent",
        iconBase = "org/rteasy/Datasource.gif",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "simstate", openAtStartup = false)
public final class SimStateTopComponent extends TopComponent {

    public SimStateTableModel model;
    private LinkedList<Memory> memories;
    private LinkedList<Storage> storages;
    private LinkedList<RegisterArray> regArrays;
    private MemoryCellRenderer memoryCellRenderer;

    public SimStateTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SimStateTopComponent.class, "CTL_SimStateTopComponent"));
        setToolTipText(NbBundle.getMessage(SimStateTopComponent.class, "HINT_SimObjectsTopComponent"));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
    }

    /**
     * Call {@link SimObjectsTopComponent#setData} before you call this method.
     *
     * @see SimObjectsTopComponent#setData(LinkedList, LinkedList, LinkedList,
     * LinkedList) setData
     */
    public void initTable() {
        try {
            JComboBox baseBox = new JComboBox();
            baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_BIN));
            baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_DEC));
            baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_HEX));
            baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_DEC2));
            baseBox.addItem(new SimObjectsBase(RTSimGlobals.BASE_HEX2));
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(baseBox));
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setToolTipText(NbBundle.getMessage(SimStateTopComponent.class, "TOOLTIP_BASE"));
            table.getColumnModel().getColumn(2).setCellRenderer(renderer);
            memoryCellRenderer = new MemoryCellRenderer();
            table.getColumnModel().getColumn(1).setCellRenderer(memoryCellRenderer);
            int headerWidth, cellWidth;
            for (int i = 0; i < 3; i++) {
                headerWidth = renderer.getWidth();
                cellWidth = table.getDefaultRenderer(model.getColumnClass(i)).
                        getTableCellRendererComponent(table, model.getLongestColumnValue(i),
                                false, false, 0, i).getPreferredSize().width + 20;
                if (headerWidth > cellWidth) {
                    cellWidth = headerWidth;
                }
                table.getColumnModel().getColumn(i).setPreferredWidth(cellWidth);
            }
            setUpMRCellEditor();
            table.setCellSelectionEnabled(true);
        } catch (NullPointerException nPE) {
            WrapperIOLog.logErr(nPE.getMessage());
        }
    }

    public void setUpMRCellEditor() {
        final RegArrayButton button = new RegArrayButton(null);
        final MemoryButton mbutton = new MemoryButton(null);
        final StorageButton storButton = new StorageButton(null);
        final MRCellEditor regarrEditor = new MRCellEditor(button, mbutton,storButton);
        table.getColumnModel().getColumn(1).setCellEditor(regarrEditor);
        MemoryButtonListener mBL = new MemoryButtonListener();
        button.addActionListener(mBL);
        mbutton.addActionListener(mBL);
        storButton.addActionListener(mBL);
    }

    class MemoryButtonListener implements ActionListener {

        /**
         * Opens an MemoryViewer/RegArrayViewer depending on the pressed button.
         *
         * @param aE
         */
        @Override
        public void actionPerformed(ActionEvent aE) {
            TopComponent tC;
            if (aE.getSource() instanceof MemoryButton) {
                tC = WindowHelper.findTopComponent(((MemoryButton) (aE.getSource())).memory);
            } else if(aE.getSource() instanceof StorageButton ) {
                tC = WindowHelper.findTopComponent(((StorageButton) (aE.getSource())).storage);
            } else {
                tC = WindowHelper.findTopComponent(((RegArrayButton) (aE.getSource())).regarray);
            }

            if (tC == null) {
                if (aE.getSource() instanceof MemoryButton) {
                    tC = new MemoryViewerTopComponent();
                    ((MemoryViewerTopComponent) tC).initMemoryViewer(((MemoryButton) (aE.getSource())).memory);
                } else if (aE.getSource() instanceof StorageButton) {
                    tC = new StorageViewerTopComponent();
                    ((StorageViewerTopComponent) tC).initStorViewer(((StorageButton) (aE.getSource())).storage);
                }else {
                    tC = new RegArrayViewerTopComponent();
                    ((RegArrayViewerTopComponent) tC).initRegArrayViewer(((RegArrayButton) (aE.getSource())).regarray);
                }
                Mode m = WindowManager.getDefault().findMode("memviewer");
                m.dockInto(tC);
                tC.open();
            }
            tC.requestActive();
        }

    }

    public void setCycleCount(int cc) {
        cycleCounterLabel.setText(Integer.toString(cc));
    }

    public void setStateCounter(int pc) {
        stateCounterLabel.setText(Integer.toString(pc));
    }

    /**
     * This method creates a model for the table.
     *
     * @param registerOrder
     * @param busOrder
     * @param memoryOrder
     * @param regArrOrder
     */
    public void setData(LinkedList registerOrder, LinkedList busOrder, LinkedList memoryOrder,LinkedList storageOrder, LinkedList regArrOrder) {
        model = new SimStateTableModel(registerOrder, busOrder, memoryOrder, storageOrder, regArrOrder, this);
        table.setModel(model);
        //table.setPreferredScrollableViewportSize(table.getPreferredSize());
        model.fireTableDataChanged();
        addMemories2MemoryList(memoryOrder);
        addStorages2StorArrayList(storageOrder);
        addRegArrays2RegArrayList(regArrOrder);
    }

    /**
     * * Adds all memories to list for updating process.
     *
     * @param memoryOrder
     */
    private void addMemories2MemoryList(LinkedList memoryOrder) {
        memories = new LinkedList();
        ListIterator it = memoryOrder.listIterator();
        Memory m;
        while (it.hasNext()) {
            m = (Memory) it.next();
            memories.add(m);
        }
    }

    /**
     * Adds all register arrays to list for updating process.
     *
     * @param regarOrder
     */
    private void addRegArrays2RegArrayList(LinkedList<RegisterArray> regarOrder) {
        regArrays = new LinkedList();
        ListIterator lit = regarOrder.listIterator();
        RegisterArray r;
        while (lit.hasNext()) {
            r = (RegisterArray) lit.next();
            regArrays.add(r);
        }
    }

    private void addStorages2StorArrayList(LinkedList<Storage> storageOrder) {
        storages = new LinkedList();
        for (Storage stor : storageOrder) {
            storages.add(stor);
        }
    }

    public void simUpdate() {
        model.fireTableDataChanged();
        TopComponent tC;
        for (Memory m : memories) {
            tC = WindowHelper.findTopComponent(m);
            if (tC != null) {
                ((MemoryViewerTopComponent) (tC)).simUpdate();
            }
        }
        for (RegisterArray rA : regArrays) {
            tC = WindowHelper.findTopComponent(rA);
            if (tC != null) {
                ((RegArrayViewerTopComponent) (tC)).simUpdate();
            }
        }
        for (Storage s : storages) {
            tC = WindowHelper.findTopComponent(s);
            if (tC != null) {
                ((StorageViewerTopComponent) tC).simUpdate();
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

        panel1 = new java.awt.Panel();
        stateLbl = new javax.swing.JLabel();
        stateCounterLabel = new javax.swing.JLabel();
        cycleCounterLabel = new javax.swing.JLabel();
        cycleLbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        stateLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(stateLbl, org.openide.util.NbBundle.getMessage(SimStateTopComponent.class, "LABEL_STATE")+":"); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(stateCounterLabel, org.openide.util.NbBundle.getMessage(SimStateTopComponent.class, "LABEL_ZERO")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(cycleCounterLabel, org.openide.util.NbBundle.getMessage(SimStateTopComponent.class, "LABEL_ZERO")); // NOI18N

        cycleLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(cycleLbl, org.openide.util.NbBundle.getMessage(SimStateTopComponent.class, "LABEL_CYCLECOUNT")+":"); // NOI18N

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(stateLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(stateCounterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cycleLbl)
                .addGap(18, 18, 18)
                .addComponent(cycleCounterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(stateLbl)
                .addComponent(stateCounterLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cycleLbl)
                .addComponent(cycleCounterLabel))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(panel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cycleCounterLabel;
    private javax.swing.JLabel cycleLbl;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    private javax.swing.JLabel stateCounterLabel;
    private javax.swing.JLabel stateLbl;
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

    public class MemoryCellRenderer extends DefaultTableCellRenderer {

        public final JButton button = new JButton(NbBundle.getMessage(SimStateTopComponent.class, "BUTTON_CONTENT"));
        private final Color valueChangedBackground = Color.YELLOW;
        private final Color oldBackground;

        MemoryCellRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
            oldBackground = getBackground();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            setBackground(oldBackground);

            if (value instanceof Memory || value instanceof RegisterArray || value instanceof Storage) {
                return button;
            } else if (((SimStateTableModel) table.getModel()).registerValueChangedAt(row, col)) {
                // catch if register has changed value
                setBackground(valueChangedBackground);
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                // setBackground(oldBackground);
                return c;
            } else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            }
        }
    }

    class MemoryButton extends JButton {

        public Memory memory;

        MemoryButton(Memory m) {
            super(NbBundle.getMessage(SimStateTopComponent.class, "BUTTON_CONTENT"));
            memory = m;
        }
    }

    class RegArrayButton extends JButton {

        public RegisterArray regarray;

        RegArrayButton(RegisterArray r) {
            super(NbBundle.getMessage(SimStateTopComponent.class, "BUTTON_CONTENT"));
            regarray = r;
        }
    }

    class StorageButton extends JButton {

        public Storage storage;

        StorageButton(Storage storage) {
            super(NbBundle.getMessage(SimStateTopComponent.class, "BUTTON_CONTENT"));
            this.storage = storage;
        }
    }

    class MRCellEditor extends DefaultCellEditor {

        RegArrayButton rbutton;
        MemoryButton mbutton;
        StorageButton storButton;

        MRCellEditor(RegArrayButton b, MemoryButton m, StorageButton s) {
            super(new JTextField());
            rbutton = b;
            mbutton = m;
            storButton = s;
            setClickCountToStart(1);
            rbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    fireEditingStopped();
                }
            });
            mbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int col) {
            if (value instanceof RegisterArray) {
                rbutton.regarray = (RegisterArray) value;

                return rbutton;
            } else if (value instanceof Memory) {
                mbutton.memory = (Memory) value;
                return mbutton;
            } else if (value instanceof Storage) {
                storButton.storage = (Storage) value;
                return storButton;
            } else {
                return super.getTableCellEditorComponent(table, value, isSelected, row, col);
            }
        }
    }
}
