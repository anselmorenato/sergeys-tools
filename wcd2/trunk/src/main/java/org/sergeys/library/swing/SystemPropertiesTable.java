package org.sergeys.library.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class SystemPropertiesTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Properties props;

	public SystemPropertiesTable(String keyTitle, String valueTitle) {
		final String[] columnNames = { keyTitle, valueTitle };

		props = System.getProperties();

		final ArrayList<Entry<Object, Object>> p = new ArrayList<Entry<Object, Object>>(
				props.entrySet());
		Collections.sort(p, new Comparator<Entry<Object, Object>>() {

			@Override
			public int compare(Entry<Object, Object> o1,
					Entry<Object, Object> o2) {
				return o1.getKey().toString().compareTo(o2.getKey().toString());
			}

		});

		AbstractTableModel tm = new AbstractTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public int getRowCount() {
				return p.size();
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public String getColumnName(int column) {
				return columnNames[column];
			};

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return (columnIndex == 0) ? p.get(rowIndex).getKey() : p.get(
						rowIndex).getValue();
			}

		};

		this.setModel(tm);
		this.getColumnModel().getColumn(0).setPreferredWidth(50);
		this.getColumnModel().getColumn(1).setPreferredWidth(100);
	}

	public Properties getSystemProperties() {
		return props;
	}

	public void setSystemProperties(Properties systemProperties) {
		this.props = systemProperties;
	}
}
