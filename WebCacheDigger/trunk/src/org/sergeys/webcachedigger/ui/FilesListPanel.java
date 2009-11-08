package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FilesListPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTableFoundFiles = null;

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
		this.setSize(300, 200);
		this.setLayout(new BorderLayout());
		this.add(getJScrollPane(), BorderLayout.CENTER);
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
		}
		return jTableFoundFiles;
	}
	
	public void init(List<CachedFile> files){
		FilesTableModel model = new FilesTableModel(files);
		getJTableFoundFiles().setModel(model);
		getJTableFoundFiles().setColumnModel(FilesListUtils.getColumnModel());
		getJTableFoundFiles().setRowSorter(new TableRowSorter<FilesTableModel>(model));
	}
}
