package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.sergeys.coverfinder.logic.Settings;

public class SettingsDialog 
extends JDialog 
{
	public static final String SETTINGS_SAVED_PROPERTY = "SETTINGS_SAVED_PROPERTY";
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();


	JList listLibraryDirs;
	DefaultListModel listModel;
	ImageSearchChooserPanel imageSearchChooserPanel;
	JButton buttonMinus;
	JCheckBox chckbxMakeBackupCopies;
	JCheckBox chckbxConfirmChangesTo;
	
	/**
	 * Create the dialog.
	 */
	public SettingsDialog(Window owner) {
		super(owner);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setIconImage(Toolkit.getDefaultToolkit().getImage(SettingsDialog.class.getResource("/images/icon.png")));
		setTitle("Settings");
		setBounds(100, 100, 450, 275);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblLibraryFolders = new JLabel("Music folders:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblLibraryFolders, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblLibraryFolders, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblLibraryFolders);
		
		JButton button = new JButton("+");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doAddDirectory();
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, button, -4, SpringLayout.NORTH, lblLibraryFolders);
		sl_contentPanel.putConstraint(SpringLayout.WEST, button, -51, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, button, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(button);
		
		buttonMinus = new JButton("-");
		buttonMinus.setEnabled(false);
		buttonMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemoveDirectory();
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, buttonMinus, 4, SpringLayout.SOUTH, button);
		sl_contentPanel.putConstraint(SpringLayout.WEST, buttonMinus, -51, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, buttonMinus, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(buttonMinus);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, scrollPane, -5, SpringLayout.NORTH, lblLibraryFolders);
		sl_contentPanel.putConstraint(SpringLayout.WEST, scrollPane, 6, SpringLayout.EAST, lblLibraryFolders);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, scrollPane, 90, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, scrollPane, -11, SpringLayout.WEST, button);
		contentPanel.add(scrollPane);
		
		listLibraryDirs = new JList();
		listLibraryDirs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listModel = new DefaultListModel();
		listLibraryDirs.setModel(listModel);
		scrollPane.setViewportView(listLibraryDirs);
		listLibraryDirs.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {				
				if(!e.getValueIsAdjusting() && e.getFirstIndex() >= 0){
					buttonMinus.setEnabled(true);			
				}
			}
		});
		
		imageSearchChooserPanel = new ImageSearchChooserPanel();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, imageSearchChooserPanel, 6, SpringLayout.SOUTH, scrollPane);
		sl_contentPanel.putConstraint(SpringLayout.WEST, imageSearchChooserPanel, 5, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, imageSearchChooserPanel, 166, SpringLayout.EAST, contentPanel);
		contentPanel.add(imageSearchChooserPanel);
		
		chckbxMakeBackupCopies = new JCheckBox("Make backup copies when changing music files");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, chckbxMakeBackupCopies, 6, SpringLayout.SOUTH, imageSearchChooserPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, chckbxMakeBackupCopies, 0, SpringLayout.WEST, contentPanel);
		contentPanel.add(chckbxMakeBackupCopies);
		
		chckbxConfirmChangesTo = new JCheckBox("Confirm changes to music files");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, chckbxConfirmChangesTo, 6, SpringLayout.SOUTH, chckbxMakeBackupCopies);
		sl_contentPanel.putConstraint(SpringLayout.WEST, chckbxConfirmChangesTo, 0, SpringLayout.WEST, chckbxMakeBackupCopies);
		contentPanel.add(chckbxConfirmChangesTo);
		
		
//		ImageSearchChooserPanel imgSearchPanel = new ImageSearchChooserPanel();
//		sl_contentPanel.putConstraint(SpringLayout.NORTH, imgSearchPanel, 6, SpringLayout.SOUTH, scrollPane);
//		sl_contentPanel.putConstraint(SpringLayout.WEST, imgSearchPanel, 0, SpringLayout.WEST, contentPanel);
//		sl_contentPanel.putConstraint(SpringLayout.SOUTH, imgSearchPanel, 59, SpringLayout.SOUTH, scrollPane);
//		sl_contentPanel.putConstraint(SpringLayout.EAST, imgSearchPanel, -195, SpringLayout.EAST, contentPanel);
//		contentPanel.add(imgSearchPanel);
		
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton saveButton = new JButton("Save");
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSave();
					}
				});
				saveButton.setActionCommand("OK");
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
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
	}

	protected void doCancel() {
		setVisible(false);		
	}

	protected void doSave() {
		// collect values
		Settings.getInstance().getLibraryPaths().clear();
		for(int i = 0; i < listModel.getSize(); i++){
			Settings.getInstance().getLibraryPaths().add((String)listModel.get(i));
		}
		
		imageSearchChooserPanel.updateValues();
		Settings.getInstance().setBackupFileOnSave(chckbxMakeBackupCopies.isSelected());
		Settings.getInstance().setConfirmFileEdit(chckbxConfirmChangesTo.isSelected());
		
		// save
		try {
			Settings.save();
			firePropertyChange(SETTINGS_SAVED_PROPERTY, null, null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setVisible(false);		
	}

	protected void doRemoveDirectory() {
		if(listLibraryDirs.getSelectedIndex() != -1){
			listModel.remove(listLibraryDirs.getSelectedIndex());
		}
		
		//buttonMinus.setEnabled(listLibraryDirs.getSelectedIndex() != -1);
		updateControlsState();
	}

	DirSelectorDialog dlg;
	
	protected void doAddDirectory() {
		if(dlg == null){
			dlg = new DirSelectorDialog(this);
			dlg.addPropertyChangeListener(DirSelectorDialog.DIRECTORY_SELECTED, new PropertyChangeListener(){

				@Override
				public void propertyChange(PropertyChangeEvent evt) {					
					if(evt.getPropertyName().equals(DirSelectorDialog.DIRECTORY_SELECTED)){
						listModel.addElement(evt.getNewValue());
					}
				}});
			
			dlg.setLocationRelativeTo(this);
		}
		dlg.setVisible(true);		
	}
	
			
	@Override	
	public void setVisible(boolean isVisible) {
		// set values
		listModel.removeAllElements();
		for(String path: Settings.getInstance().getLibraryPaths()){
			listModel.addElement(path);			
		}
		
		imageSearchChooserPanel.initValues();
		chckbxConfirmChangesTo.setSelected(Settings.getInstance().isConfirmFileEdit());
		chckbxMakeBackupCopies.setSelected(Settings.getInstance().isBackupFileOnSave());
				
		updateControlsState();
		
		super.setVisible(isVisible);
	}
	
	private void updateControlsState(){
		buttonMinus.setEnabled(listLibraryDirs.getSelectedIndex() != -1);
	}
}
