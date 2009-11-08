package org.sergeys.webcachedigger.ui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class FilesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<File> files;

	public FilesTableModel(ArrayList<File> files) {
		this.files = files;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return files.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
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
