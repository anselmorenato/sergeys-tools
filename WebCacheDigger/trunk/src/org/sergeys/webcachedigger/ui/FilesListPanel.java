package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FilesListPanel extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private JScrollPane jScrollPane = null;
	private JTable jTableFoundFiles = null;

	private DefaultListSelectionModel foundFilesSelectionModel; // @jve:decl-index=0:

	private CachedFile oldCachedFile;
	private CachedFile selectedCachedFile; // @jve:decl-index=0:

	/**
	 * @return the selectedCachedFile
	 */
	public CachedFile getSelectedCachedFile() {
		return selectedCachedFile;
	}

	/**
	 * @param selectedCachedFile
	 *            the selectedCachedFile to set
	 */
	public void setSelectedCachedFile(CachedFile selectedCachedFile) {
		this.selectedCachedFile = selectedCachedFile;
	}

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
			jTableFoundFiles
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// jTableFoundFiles.addMouseListener(new
			// java.awt.event.MouseAdapter() {
			// public void mouseClicked(java.awt.event.MouseEvent e) {
			// FilesListPanel.this.mouseClicked(e);
			// }
			// });
		}
		return jTableFoundFiles;
	}

	public void init(List<CachedFile> files) {
		FilesTableModel model = new FilesTableModel(files);
		getJTableFoundFiles().setModel(model);
		getJTableFoundFiles().setColumnModel(FilesListUtils.getColumnModel());
		getJTableFoundFiles().setRowSorter(
				new TableRowSorter<FilesTableModel>(model));
		// getJTableFoundFiles().setSelectionModel(new
		// FilesTableSelectionModel());
		getJTableFoundFiles().setSelectionModel(getFoundFilesSelectionModel());

		getFoundFilesSelectionModel().addListSelectionListener(this);
	}

	private void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			//

			int rowNo = getJTableFoundFiles().getSelectedRow();
			int modelRowNo = getJTableFoundFiles()
					.convertRowIndexToModel(rowNo);
			FilesTableModel model = (FilesTableModel) getJTableFoundFiles()
					.getModel();
			CachedFile cf = model.getCachedFile(modelRowNo);

			// MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
			// Collection mt = MimeUtil.getMimeTypes(cf);

			String msg = String
					.format("%s\n%s", cf.getName(), cf.getFileType());
			JOptionPane.showMessageDialog(this, msg);

			// if(CachedFile.extensionByMimetype.containsKey(cf.getFileType())){
			if (cf.guessExtension() != null) {
				try {
					// File tmp = File.createTempFile("wcd", "." +
					// CachedFile.extensionByMimetype.get(cf.getFileType()));
					File tmp = File.createTempFile("wcd", "."
							+ cf.guessExtension());
					CachedFile.copyFile(cf, tmp);
					Desktop.getDesktop().open(tmp);
					if (JOptionPane.showConfirmDialog(this, "Delete temp file "
							+ tmp.getAbsolutePath() + "?") == JOptionPane.YES_OPTION) {
						tmp.delete();
					}

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this,
							"Failed to preview file: " + ex.getMessage());
					ex.printStackTrace();
				}
			}

			// copy file
			// File.createTempFile("wcd", suffix)

			// Desktop.getDesktop().open(cf);
		}
	}

	public List<CachedFile> getCachedFiles() {
		return ((FilesTableModel) getJTableFoundFiles().getModel())
				.getCachedFiles();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO: something is wrong. Some events are lost.
		try {
			oldCachedFile = getSelectedCachedFile();
			CachedFile newFile = ((FilesTableModel) getJTableFoundFiles()
					.getModel()).getCachedFile(e.getFirstIndex());
			setSelectedCachedFile(newFile);

			firePropertyChange("selectedfile", oldCachedFile, newFile);
		} catch (ArrayIndexOutOfBoundsException ex) {

		}

		// TODO: make named constant
		// SwingUtilities.invokeLater(new Runnable(){
		//
		// @Override
		// public void run() {
		// FilesListPanel.this.firePropertyChange("selectedfile",
		// FilesListPanel.this.oldCachedFile,
		// FilesListPanel.this.getSelectedCachedFile());
		// }
		//			
		// });
	}

} // @jve:decl-index=0:visual-constraint="10,10"
