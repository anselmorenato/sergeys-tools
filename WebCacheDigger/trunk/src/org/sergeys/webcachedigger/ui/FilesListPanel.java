package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableRowSorter;

import org.sergeys.webcachedigger.logic.CachedFile;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Uncomment line in initialize() to get panel components in ui designer

public class FilesListPanel extends JPanel implements ListSelectionListener {
	
	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTableFoundFiles = null;

	private DefaultListSelectionModel foundFilesSelectionModel;
	
	private JPopupMenu popupMenu = new JPopupMenu();
	
	private static final String CHECK_ALL = "CHECK_ALL";
	private static final String UNCHECK_ALL = "UNCHECK_ALL";
	private static final String CHECK_ALL_TYPE = "CHECK_ALL_TYPE";
	private static final String UNCHECK_ALL_TYPE = "UNCHECK_ALL_TYPE";
	
	
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
		this.setPreferredSize(new Dimension(600, 300));
		this.add(getJScrollPane(), BorderLayout.CENTER);
		
		
		// TODO: uncomment this line to get panel components in ui designer,
		// comment for production.		
		//add(popupMenu, BorderLayout.EAST);
		
		
		JMenuItem mntmCheckAllOf = new JMenuItem("Check all of type");
		mntmCheckAllOf.setName(CHECK_ALL_TYPE);
		mntmCheckAllOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPopupMenuItemSelected(e);
			}
		});
		popupMenu.add(mntmCheckAllOf);
		
		JMenuItem mntmUncheckAllOf = new JMenuItem("Uncheck all of type");
		mntmUncheckAllOf.setName(UNCHECK_ALL_TYPE);
		mntmUncheckAllOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPopupMenuItemSelected(e);
			}
		});
		popupMenu.add(mntmUncheckAllOf);
		
		JSeparator separator = new JSeparator();
		popupMenu.add(separator);
		
		JMenuItem mntmCheckAll = new JMenuItem("Check all");
		mntmCheckAll.setName(CHECK_ALL);
		mntmCheckAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPopupMenuItemSelected(e);
			}
		});
		popupMenu.add(mntmCheckAll);
		
		JMenuItem mntmUncheckAll = new JMenuItem("Uncheck all");
		mntmUncheckAll.setName(UNCHECK_ALL);
		mntmUncheckAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPopupMenuItemSelected(e);
			}
		});
		popupMenu.add(mntmUncheckAll);
								
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

}
