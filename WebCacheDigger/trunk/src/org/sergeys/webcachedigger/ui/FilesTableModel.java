package org.sergeys.webcachedigger.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FilesTableModel extends AbstractTableModel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {

		if(columnIndex == 0){
			cachedFiles.get(rowIndex).setSelectedToCopy((Boolean)value);
		}
		else{
			super.setValueAt(value, rowIndex, columnIndex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0;
	}

	private static final long serialVersionUID = 1L;
	private List<CachedFile> cachedFiles;

	/**
	 * @return the cachedFiles
	 */
	public List<CachedFile> getCachedFiles() {
		return cachedFiles;
	}

	public FilesTableModel(List<CachedFile> files) {
		this.cachedFiles = files;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {

		return cachedFiles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return this.cachedFiles.get(rowIndex).isSelectedToCopy();
		case 1:
			return this.cachedFiles.get(rowIndex).getName();
		case 2:
			return this.cachedFiles.get(rowIndex).getFileType();
		case 3:
			return this.cachedFiles.get(rowIndex).length();
		case 4:
			// format long as datetime
			return String.format("%1$tF %1$tR", this.cachedFiles.get(rowIndex)
					.lastModified());
		default:
			return null;
		}

	}

	public CachedFile getCachedFile(int rowIndex) {
		return this.cachedFiles.get(rowIndex);
	}
	
}
