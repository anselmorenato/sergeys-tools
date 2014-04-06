package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Messages;

// Uncomment line in initialize() to get panel components in ui designer

public class FilesListPanel
extends JPanel
implements ListSelectionListener, TableModelListener
{

    private static final long serialVersionUID = 1L;
    private JScrollPane jScrollPane = null;
    private JTable jTableFoundFiles = null;

    private DefaultListSelectionModel foundFilesSelectionModel;

    private JPopupMenu popupMenu = new JPopupMenu();

    private static final String CHECK_ALL = "CHECK_ALL"; //$NON-NLS-1$
    private static final String UNCHECK_ALL = "UNCHECK_ALL"; //$NON-NLS-1$
    private static final String CHECK_ALL_TYPE = "CHECK_ALL_TYPE"; //$NON-NLS-1$
    private static final String UNCHECK_ALL_TYPE = "UNCHECK_ALL_TYPE"; //$NON-NLS-1$

    JLabel lblTotal;
    JLabel lblChecked;

    /**
     * @return the foundFilesSelectionModel
     */
    public DefaultListSelectionModel getFoundFilesSelectionModel() {
        if (foundFilesSelectionModel == null) {
            foundFilesSelectionModel = new DefaultListSelectionModel();
        }
        return foundFilesSelectionModel;
    }

    /**
     * This is the default constructor
     */
    public FilesListPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(578, 187);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(432, 300));
        this.add(getJScrollPane(), BorderLayout.CENTER);


        // TODO: uncomment this line to get panel components in ui designer,
        // keep commented for production.
//		add(popupMenu, BorderLayout.EAST);


        JMenuItem mntmCheckAllOf = new JMenuItem(Messages.getString("FilesListPanel.checkAllType")); //$NON-NLS-1$
        mntmCheckAllOf.setName(CHECK_ALL_TYPE);
        mntmCheckAllOf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPopupMenuItemSelected(e);
            }
        });
        popupMenu.add(mntmCheckAllOf);

        JMenuItem mntmUncheckAllOf = new JMenuItem(Messages.getString("FilesListPanel.uncheckAllType")); //$NON-NLS-1$
        mntmUncheckAllOf.setName(UNCHECK_ALL_TYPE);
        mntmUncheckAllOf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPopupMenuItemSelected(e);
            }
        });
        popupMenu.add(mntmUncheckAllOf);

        JSeparator separator = new JSeparator();
        popupMenu.add(separator);

        JMenuItem mntmCheckAll = new JMenuItem(Messages.getString("FilesListPanel.checkAll")); //$NON-NLS-1$
        mntmCheckAll.setName(CHECK_ALL);
        mntmCheckAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPopupMenuItemSelected(e);
            }
        });
        popupMenu.add(mntmCheckAll);

        JMenuItem mntmUncheckAll = new JMenuItem(Messages.getString("FilesListPanel.uncheckAll")); //$NON-NLS-1$
        mntmUncheckAll.setName(UNCHECK_ALL);
        mntmUncheckAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doPopupMenuItemSelected(e);
            }
        });
        popupMenu.add(mntmUncheckAll);

        JPanel panelSummary = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panelSummary.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        add(panelSummary, BorderLayout.SOUTH);

        JLabel lblTotalLabel = new JLabel(Messages.getString("FilesListPanel.lblTotal.text")); //$NON-NLS-1$
        panelSummary.add(lblTotalLabel);

        lblTotal = new JLabel(Messages.getString("FilesListPanel.0"));  //$NON-NLS-1$
        panelSummary.add(lblTotal);

        JLabel lblCheckedLabel = new JLabel(Messages.getString("FilesListPanel.lblCheckedToCopy.text")); //$NON-NLS-1$
        panelSummary.add(lblCheckedLabel);

        lblChecked = new JLabel(Messages.getString("FilesListPanel.1"));  //$NON-NLS-1$
        panelSummary.add(lblChecked);

        lblTotal.setText("0"); //$NON-NLS-1$
        lblChecked.setText("0"); //$NON-NLS-1$
    }

    protected void doPopupMenuItemSelected(ActionEvent e) {
        if(((JMenuItem)e.getSource()).getName().equals(CHECK_ALL)){
            ((FilesTableModel)jTableFoundFiles.getModel()).checkAll(true);
        }
        else if(((JMenuItem)e.getSource()).getName().equals(UNCHECK_ALL)){
            ((FilesTableModel)jTableFoundFiles.getModel()).checkAll(false);
        }
        else if(((JMenuItem)e.getSource()).getName().equals(CHECK_ALL_TYPE)){
            String mimeType = (String) jTableFoundFiles.getValueAt(jTableFoundFiles.getSelectedRow(), 2);
            ((FilesTableModel)jTableFoundFiles.getModel()).checkByType(mimeType, true);
        }
        else if(((JMenuItem)e.getSource()).getName().equals(UNCHECK_ALL_TYPE)){
            String mimeType = (String) jTableFoundFiles.getValueAt(jTableFoundFiles.getSelectedRow(), 2);
            ((FilesTableModel)jTableFoundFiles.getModel()).checkByType(mimeType, false);
        }
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTableFoundFiles());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTableFoundFiles
     *
     * @return javax.swing.JTable
     */
    private JTable getJTableFoundFiles() {
        if (jTableFoundFiles == null) {
            jTableFoundFiles = new JTable();
            jTableFoundFiles.addMouseListener(new MouseAdapter() {

                // handle both pressed and released for popup

                @Override
                public void mouseReleased(MouseEvent e) {
                    doPopupMenu(e);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    doPopupMenu(e);
                }
            });

            jTableFoundFiles
                    .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // speed up: http://www.chka.de/swing/table/faq.html
            ToolTipManager.sharedInstance().unregisterComponent(jTableFoundFiles);
            ToolTipManager.sharedInstance().unregisterComponent(jTableFoundFiles.getTableHeader());
        }
        return jTableFoundFiles;
    }

    protected void doPopupMenu(MouseEvent e) {
        if(e.isPopupTrigger()){
            JTable source = (JTable)e.getSource();
            int row = source.rowAtPoint( e.getPoint() );
            int column = source.columnAtPoint( e.getPoint() );

            if (! source.isRowSelected(row)){
                source.changeSelection(row, column, false, false);
            }

            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getJTableFoundFiles().setEnabled(enabled);
    };

    public void init(List<CachedFile> files) {
        FilesTableModel model = new FilesTableModel(files);
        getJTableFoundFiles().setModel(model);
        getJTableFoundFiles().setColumnModel(FilesListUtils.getColumnModel());
        getJTableFoundFiles().setRowSorter(
                new TableRowSorter<FilesTableModel>(model));
        getJTableFoundFiles().setSelectionModel(getFoundFilesSelectionModel());

        getFoundFilesSelectionModel().addListSelectionListener(this);
        getJTableFoundFiles().getModel().addTableModelListener(this);

        ArrayList<RowSorter.SortKey> sort = new ArrayList<RowSorter.SortKey>();
        sort.add(new SortKey(4, SortOrder.DESCENDING));
        getJTableFoundFiles().getRowSorter().setSortKeys(sort);

        lblTotal.setText(String.valueOf(files.size()));
        lblChecked.setText("0"); //$NON-NLS-1$
    }

    public void filesListChanged(){
        ((FilesTableModel) getJTableFoundFiles().getModel()).fireTableDataChanged();
    }

    public List<CachedFile> getCachedFiles() {
        return ((FilesTableModel) getJTableFoundFiles().getModel())
                .getCachedFiles();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

        if(!e.getValueIsAdjusting()){
            try {

                int rowNo = getJTableFoundFiles().getSelectedRow();
                int modelRowNo = getJTableFoundFiles()
                    .convertRowIndexToModel(rowNo);

                CachedFile newFile = ((FilesTableModel) getJTableFoundFiles()
                        .getModel()).getCachedFile(modelRowNo);
                firePropertyChange(CachedFile.SELECTED_FILE, null, newFile);

            } catch (ArrayIndexOutOfBoundsException ex) {

            }
            catch (IndexOutOfBoundsException ex) {
                // occurs when model is emptied before next search
            }
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {

        if(e.getColumn() == 0 || e.getColumn() == TableModelEvent.ALL_COLUMNS){
            // file checked/unchecked
            lblChecked.setText(String.valueOf(
                    ((FilesTableModel)getJTableFoundFiles().getModel()).getCheckedCount()
                    )
            );
        }
    }

}
