package org.sergeys.webcachedigger.ui;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class FilesListUtils {
	public static TableColumnModel getColumnModel(){
		TableColumnModel model = new DefaultTableColumnModel();
		
		TableColumn column = new TableColumn();
		column.setHeaderValue("");		
		model.addColumn(column);
		
		column = new TableColumn();
		column.setHeaderValue("File");		
		model.addColumn(column);
		
		column = new TableColumn();
		column.setHeaderValue("Type");		
		model.addColumn(column);
		
		return model;
	}
}
