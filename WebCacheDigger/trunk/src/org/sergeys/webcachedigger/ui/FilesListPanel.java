package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import org.sergeys.webcachedigger.logic.CachedFile;
import javax.swing.ListSelectionModel;

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
			jTableFoundFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableFoundFiles.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {					
					FilesListPanel.this.mouseClicked(e);
				}
			});
		}
		return jTableFoundFiles;
	}
	
	public void init(List<CachedFile> files){
		FilesTableModel model = new FilesTableModel(files);
		getJTableFoundFiles().setModel(model);
		getJTableFoundFiles().setColumnModel(FilesListUtils.getColumnModel());
		getJTableFoundFiles().setRowSorter(new TableRowSorter<FilesTableModel>(model));
	}
	
	private void mouseClicked(MouseEvent e){
		if(e.getClickCount() == 2){
			//
			
			int rowNo = getJTableFoundFiles().getSelectedRow();
			int modelRowNo = getJTableFoundFiles().convertRowIndexToModel(rowNo);
			FilesTableModel model = (FilesTableModel)getJTableFoundFiles().getModel();
			CachedFile cf = model.getCachedFile(modelRowNo);
			
			//MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
			//Collection mt = MimeUtil.getMimeTypes(cf);

			String msg = String.format("%s\n%s", cf.getName(), cf.getFileType());
			JOptionPane.showMessageDialog(this, msg);
			
			if(CachedFile.extensionByMimetype.containsKey(cf.getFileType())){
				try {
					File tmp = File.createTempFile("wcd", "." + CachedFile.extensionByMimetype.get(cf.getFileType()));
					CachedFile.copyFile(cf, tmp);
					Desktop.getDesktop().open(tmp);
					if(JOptionPane.showConfirmDialog(this, "Delete temp file " + tmp.getAbsolutePath() + "?") == JOptionPane.YES_OPTION){
						tmp.delete();
					}
					
				} catch (IOException ex) {					
					JOptionPane.showMessageDialog(this, "Failed to preview file: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
			
			// copy file
			//File.createTempFile("wcd", suffix)
			
			//Desktop.getDesktop().open(cf);
		}
	}
}
