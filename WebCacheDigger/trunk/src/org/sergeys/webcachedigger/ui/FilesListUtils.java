package org.sergeys.webcachedigger.ui;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.sergeys.webcachedigger.logic.Messages;


public class FilesListUtils {
	public static TableColumnModel getColumnModel(){
		TableColumnModel model = new DefaultTableColumnModel();
		
		TableColumn column = new TableColumn();
		column.setHeaderValue(Messages.getString("FilesListUtils.copy"));		 //$NON-NLS-1$
		column.setModelIndex(0);
		model.addColumn(column);
		
		column = new TableColumn();
		column.setHeaderValue(Messages.getString("FilesListUtils.fileName")); //$NON-NLS-1$
		column.setModelIndex(1);
		model.addColumn(column);
		
		column = new TableColumn();
		column.setHeaderValue(Messages.getString("FilesListUtils.type")); //$NON-NLS-1$
		column.setModelIndex(2);
		model.addColumn(column);

		column = new TableColumn();
		column.setHeaderValue(Messages.getString("FilesListUtils.size")); //$NON-NLS-1$
		column.setModelIndex(3);
		model.addColumn(column);

		column = new TableColumn();
		column.setHeaderValue(Messages.getString("FilesListUtils.lastModified")); //$NON-NLS-1$
		column.setModelIndex(4);
		model.addColumn(column);
		
		return model;
	}
}
