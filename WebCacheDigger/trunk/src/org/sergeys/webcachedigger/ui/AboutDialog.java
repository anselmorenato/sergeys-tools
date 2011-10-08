package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTabbedPane jTabbedPaneCenter = null;
	private JPanel jPanelBottom = null;
	private JButton jButtonOK = null;
	private JPanel jPanelAbout = null;
	private JLabel jLabelTitle = null;
	private JLabel jLabelVersion = null;
	private JLabel jLabelAuthor = null;
	private JPanel jPanelSystem = null;
	private JPanel jPanelLibraries = null;
	private JTextArea jTextAreaLibraries = null;
	private JScrollPane jScrollPaneSystemProperties = null;
	private JTable jTableSystemProperties = null;
	private JButton jButtonSave = null;
	private JPanel jPanelSystemActions = null;
	private JButton jButtonSubmit = null;

	/**
	 * @param owner
	 */
	public AboutDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 300);
		this.setPreferredSize(new Dimension(500, 300));
		this.setTitle("About");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJTabbedPaneCenter(), BorderLayout.CENTER);
			jContentPane.add(getJPanelBottom(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTabbedPaneCenter	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPaneCenter() {
		if (jTabbedPaneCenter == null) {
			jTabbedPaneCenter = new JTabbedPane();
			jTabbedPaneCenter.addTab("About", null, getJPanelAbout(), null);
			jTabbedPaneCenter.addTab("System", null, getJPanelSystem(), null);
			jTabbedPaneCenter.addTab("Libraries", null, getJPanelLibraries(), null);
		}
		return jTabbedPaneCenter;
	}

	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setVgap(0);
			flowLayout.setAlignment(FlowLayout.RIGHT);
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(flowLayout);
			jPanelBottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			jPanelBottom.add(getJButtonOK(), null);
		}
		return jPanelBottom;
	}

	/**
	 * This method initializes jButtonOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText("OK");
			jButtonOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jPanelAbout	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelAbout() {
		if (jPanelAbout == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			jLabelAuthor = new JLabel();
			jLabelAuthor.setText("Sergey Selivanov");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			jLabelVersion = new JLabel();
			//jLabelVersion.setText("Dec 2 2009: " + CachedFile.junkMessage());
			//jLabelVersion.setText("Dec 26 2009");
			jLabelVersion.setText("Oct 8 2011");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jLabelTitle = new JLabel();
			jLabelTitle.setText("Web Cache Digger");
			jPanelAbout = new JPanel();
			jPanelAbout.setLayout(new GridBagLayout());
			jPanelAbout.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			jPanelAbout.add(jLabelTitle, gridBagConstraints1);
			jPanelAbout.add(jLabelVersion, gridBagConstraints2);
			jPanelAbout.add(jLabelAuthor, gridBagConstraints3);
		}
		return jPanelAbout;
	}

	/**
	 * This method initializes jPanelSystem	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSystem() {
		if (jPanelSystem == null) {
			jPanelSystem = new JPanel();
			jPanelSystem.setLayout(new BorderLayout());
			jPanelSystem.add(getJScrollPaneSystemProperties(), BorderLayout.CENTER);
			jPanelSystem.add(getJPanelSystemActions(), BorderLayout.SOUTH);
		}
		return jPanelSystem;
	}

	/**
	 * This method initializes jPanelLibraries	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelLibraries() {
		if (jPanelLibraries == null) {
			jPanelLibraries = new JPanel();
			jPanelLibraries.setLayout(new BorderLayout());
			jPanelLibraries.add(getJTextAreaLibraries(), BorderLayout.CENTER);
		}
		return jPanelLibraries;
	}

	/**
	 * This method initializes jTextAreaLibraries	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextAreaLibraries() {
		if (jTextAreaLibraries == null) {
			jTextAreaLibraries = new JTextArea();
			jTextAreaLibraries.setEditable(false);
		}
		return jTextAreaLibraries;
	}

	/**
	 * This method initializes jScrollPaneSystemProperties	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneSystemProperties() {
		if (jScrollPaneSystemProperties == null) {
			jScrollPaneSystemProperties = new JScrollPane();
			jScrollPaneSystemProperties.setViewportView(getJTableSystemProperties());
		}
		return jScrollPaneSystemProperties;
	}

	/**
	 * This method initializes jTableSystemProperties	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableSystemProperties() {
		if (jTableSystemProperties == null) {
			final String[] columnNames = {
				"Property",
				"Value"
			};
			
			Properties props = System.getProperties();
//			final ArrayList<String> keys = new ArrayList<String>();
//			for(Entry<Object, Object> key: props.entrySet()){
//				keys.add(key.getKey().toString());				
//			}
			
			final ArrayList<Entry<Object, Object>> p = new ArrayList<Entry<Object, Object>>(props.entrySet());			
			Collections.sort(p, new Comparator<Entry<Object, Object>>(){

				@Override
				public int compare(Entry<Object, Object> o1,
						Entry<Object, Object> o2) {
					return o1.getKey().toString().compareTo(o2.getKey().toString());
				}
				
			});
			
			AbstractTableModel tm = new AbstractTableModel(){

				/**
				 * 
				 */
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
					// TODO Auto-generated method stub
					return (columnIndex == 0) ? p.get(rowIndex).getKey() : p.get(rowIndex).getValue();
				}
				
			};
			
			jTableSystemProperties = new JTable(tm);
			jTableSystemProperties.getColumnModel().getColumn(0).setPreferredWidth(50);
			jTableSystemProperties.getColumnModel().getColumn(1).setPreferredWidth(100);
		}
		return jTableSystemProperties;
	}

	/**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setEnabled(false);
			jButtonSave.setText("Save to File...");
		}
		return jButtonSave;
	}

	/**
	 * This method initializes jPanelSystemActions	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSystemActions() {
		if (jPanelSystemActions == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.RIGHT);
			jPanelSystemActions = new JPanel();
			jPanelSystemActions.setLayout(flowLayout1);
			jPanelSystemActions.add(getJButtonSave(), null);
			jPanelSystemActions.add(getJButtonSubmit(), null);
		}
		return jPanelSystemActions;
	}

	/**
	 * This method initializes jButtonSubmit	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSubmit() {
		if (jButtonSubmit == null) {
			jButtonSubmit = new JButton();
			jButtonSubmit.setEnabled(false);
			jButtonSubmit.setText("Submit...");
		}
		return jButtonSubmit;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
