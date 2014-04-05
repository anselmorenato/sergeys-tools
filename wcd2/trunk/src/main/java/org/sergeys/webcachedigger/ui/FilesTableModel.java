package org.sergeys.webcachedigger.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FilesTableModel 
extends AbstractTableModel 
//extends DefaultTableModel
{

	private int checkedCount;
	
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
			checkedCount = (Boolean)value ? checkedCount + 1 : checkedCount - 1; 
		}
		else{
			super.setValueAt(value, rowIndex, columnIndex);
		}
		
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return (cachedFiles.size() == 0) ? Object.class : getValueAt(0, columnIndex).getClass();
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
		setCheckedCount(0);
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {

		return (cachedFiles == null) ? 0 : cachedFiles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		if(this.cachedFiles.size() == 0){
			return null;
		}
		
		switch (columnIndex) {
		case 0:
			return this.cachedFiles.get(rowIndex).isSelectedToCopy();
		case 1:
			return this.cachedFiles.get(rowIndex).getName();
		case 2:
			return this.cachedFiles.get(rowIndex).getMimeType();
		case 3:
			return this.cachedFiles.get(rowIndex).length();
		case 4:
			// format long as datetime
			return String.format("%1$tF %1$tR", this.cachedFiles.get(rowIndex) //$NON-NLS-1$
					.lastModified());
		default:
			return null;
		}

	}

	public CachedFile getCachedFile(int rowIndex) {
		return this.cachedFiles.get(rowIndex);
	}
	
	public void checkAll(boolean checked){		
		for(CachedFile f: cachedFiles){
			f.setSelectedToCopy(checked);					
		}
		
		checkedCount = checked ? cachedFiles.size() : 0;
		
		fireTableDataChanged();				
	}		
	
	public void checkByType(String mimeType, boolean checked){
		int i = 0;
		for(CachedFile f: cachedFiles){
			if(f.getMimeType().equals(mimeType)){
				
				if(f.isSelectedToCopy() != checked){
					checkedCount = checked ? checkedCount + 1 : checkedCount - 1;
					f.setSelectedToCopy(checked);
					fireTableCellUpdated(i, 0);					
				}
								
			}
			i++;
		}										
	}

	public int getCheckedCount() {
		return checkedCount;
	}

	public void setCheckedCount(int checkedCount) {
		this.checkedCount = checkedCount;
	}
		
}
