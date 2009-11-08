package org.sergeys.webcachedigger.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class FilesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<File> files;

	public FilesTableModel(List<File> files) {
		this.files = files;
	}

	@Override
	public int getColumnCount() {
		return 3;
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
			return this.files.get(rowIndex).length();
		default:
			return null;
		}

	}

}
