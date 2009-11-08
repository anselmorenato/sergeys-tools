package org.sergeys.webcachedigger.ui;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class FilesListUtils {
	public static TableColumnModel getColumnModel(){
		TableColumnModel model = new DefaultTableColumnModel();
		
		TableColumn column = new TableColumn();
		column.setHeaderValue("");		
		column.setModelIndex(0);
		model.addColumn(column);
		
		column = new TableColumn();
		column.setHeaderValue("File");
		column.setModelIndex(1);
		model.addColumn(column);
		
		column = new TableColumn();
		column.setHeaderValue("Type");
		column.setModelIndex(2);
		model.addColumn(column);
		
		return model;
	}
}
