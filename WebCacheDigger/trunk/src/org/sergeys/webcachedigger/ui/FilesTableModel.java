package org.sergeys.webcachedigger.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FilesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<CachedFile> files;

	public FilesTableModel(List<CachedFile> files) {
		this.files = files;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {

		return files.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		switch (columnIndex) {
		case 0:
			return "[x]";
		case 1:
			return this.files.get(rowIndex).getName();
		case 2:
			return this.files.get(rowIndex).getFileType();
		case 3:
			return this.files.get(rowIndex).length();
		case 4:
			// format long as datetime
			return String.format("%1$tF %1$tR", this.files.get(rowIndex).lastModified());			
		default:
			return null;
		}

	}
	
	public CachedFile getCachedFile(int rowIndex){
		return this.files.get(rowIndex);
	}

}
