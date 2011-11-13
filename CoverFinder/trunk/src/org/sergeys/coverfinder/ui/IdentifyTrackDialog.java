package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.sergeys.coverfinder.logic.IdentifyTrackResult;

public class IdentifyTrackDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tableResults;


	/**
	 * Create the dialog.
	 */
	public IdentifyTrackDialog(List<IdentifyTrackResult> result, Window owner) {
		super(owner);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(IdentifyTrackDialog.class.getResource("/images/icon.png")));
		setTitle("Identify track");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 593, 233);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doOK();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doCancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				tableResults = new JTable();
				tableResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane.setViewportView(tableResults);
			}
		}
		
		setupTable(result);
	}


	protected void doCancel() {
		setVisible(false);
	}


	protected void doOK() {
		setVisible(false);		
	}


	private void setupTable(final List<IdentifyTrackResult> result) {
		tableResults.setModel(new DefaultTableModel(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private String[] columnName = { "Choose", "Score", "Title", "Artist", "Link" };
			private Class<?>[] columnClass = { Boolean.class, String.class, String.class, String.class, String.class };
			
			private int checkedRow = 0;
			
			@Override
			public void setValueAt(Object aValue, int row, int column) {
				if(column == 0){
					if((Boolean)aValue){
						checkedRow = row;
					}
				}				

				fireTableChanged(new TableModelEvent(this, 0, result.size() - 1, 0));
			}

			@Override
			public int getRowCount() {				
				return result.size();
			}

			@Override
			public int getColumnCount() {				
				return 5;
			}

			@Override
			public String getColumnName(int column) {				
				return columnName[column];
			}

			@Override
			public boolean isCellEditable(int row, int column) {				
				return (column == 0);
			}

			@Override
			public Object getValueAt(int row, int column) {
				switch(column){
					case 0:
						return row == checkedRow;
					case 1:
						return result.get(row).getScore();
					case 2:
						return result.get(row).getTitle();
					case 3:
						return result.get(row).getArtist();
					case 4:
						return result.get(row).getMbid();
					default:
						return null;
				}							
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {				
				return columnClass[columnIndex];
			}});

		tableResults.getColumnModel().getColumn(0).setPreferredWidth(1000);
		tableResults.getColumnModel().getColumn(1).setPreferredWidth(2000);
		tableResults.getColumnModel().getColumn(2).setPreferredWidth(4000);
		tableResults.getColumnModel().getColumn(3).setPreferredWidth(4000);
		tableResults.getColumnModel().getColumn(4).setPreferredWidth(1000);
		
		tableResults.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void setValue(Object value) {
				// simulate hyperlink				
				setText(String.format("<html><a href=\"\">details</a></html>", value));
			}});
		
		tableResults.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int col = tableResults.columnAtPoint(e.getPoint());
				if(col == 4){
					int row = tableResults.rowAtPoint(e.getPoint());
					
					String mbid = (String)tableResults.getValueAt(row, col);
					
					try {
						Desktop.getDesktop().browse(new URI(String.format("http://musicbrainz.org/recording/%s", mbid)));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}				
			}
			
		});
	}

}
