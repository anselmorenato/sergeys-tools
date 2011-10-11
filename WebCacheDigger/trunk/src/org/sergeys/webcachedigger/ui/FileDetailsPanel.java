package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FileDetailsPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	
	JLabel lblFileName;
	JLabel lblFileSize;
	JLabel lblFiletype;
	JPanel panelPreview;
	
	/**
	 * This is the default constructor
	 */
	public FileDetailsPanel() {
		super();
		setLayout(new BorderLayout(30, 0));
		
		JPanel panelTop = new JPanel();
		panelTop.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(panelTop, BorderLayout.NORTH);
		GridBagLayout gbl_panelTop = new GridBagLayout();
		gbl_panelTop.columnWidths = new int[]{0, 0, 0};
		gbl_panelTop.rowHeights = new int[]{0, 0,0,0};
		gbl_panelTop.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelTop.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelTop.setLayout(gbl_panelTop);
		
		JLabel lblFname = new JLabel("Name:");
		GridBagConstraints gbc_lblFname = new GridBagConstraints();
		gbc_lblFname.anchor = GridBagConstraints.EAST;
		gbc_lblFname.insets = new Insets(0, 0, 5, 5);
		gbc_lblFname.gridx = 0;
		gbc_lblFname.gridy = 0;
		panelTop.add(lblFname, gbc_lblFname);
		
		lblFileName = new JLabel("filename");
		GridBagConstraints gbc_lblFileName = new GridBagConstraints();
		gbc_lblFileName.anchor = GridBagConstraints.WEST;
		gbc_lblFileName.insets = new Insets(0, 0, 5, 0);
		gbc_lblFileName.gridx = 1;
		gbc_lblFileName.gridy = 0;
		panelTop.add(lblFileName, gbc_lblFileName);
		
		JLabel lblFsize = new JLabel("Size:");
		GridBagConstraints gbc_lblFsize = new GridBagConstraints();
		gbc_lblFsize.anchor = GridBagConstraints.EAST;
		gbc_lblFsize.insets = new Insets(0, 0, 5, 5);
		gbc_lblFsize.gridx = 0;
		gbc_lblFsize.gridy = 2;
		panelTop.add(lblFsize, gbc_lblFsize);
		
		lblFileSize = new JLabel("filesize");
		GridBagConstraints gbc_lblFilesize = new GridBagConstraints();
		gbc_lblFilesize.anchor = GridBagConstraints.WEST;
		gbc_lblFilesize.insets = new Insets(0, 0, 5, 0);
		gbc_lblFilesize.gridx = 1;
		gbc_lblFilesize.gridy = 2;
		panelTop.add(lblFileSize, gbc_lblFilesize);
		
		JLabel lblType = new JLabel("Type:");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.insets = new Insets(0, 0, 0, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 3;
		panelTop.add(lblType, gbc_lblType);
		
		lblFiletype = new JLabel("filetype");
		GridBagConstraints gbc_lblFiletype = new GridBagConstraints();
		gbc_lblFiletype.anchor = GridBagConstraints.WEST;
		gbc_lblFiletype.gridx = 1;
		gbc_lblFiletype.gridy = 3;
		panelTop.add(lblFiletype, gbc_lblFiletype);
		
		panelPreview = new JPanel();
		panelPreview.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(panelPreview, BorderLayout.CENTER);
		
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(384, 285);
		this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		if(evt.getPropertyName() == CachedFile.SELECTED_FILE){
			CachedFile file = (CachedFile)evt.getNewValue();
			lblFileName.setText(file.getName());
			lblFileSize.setText(String.valueOf(file.length()));
			lblFiletype.setText(file.getFileType());
			
			remove(panelPreview);
			
			AbstractFilePreviewPanel preview = AbstractFilePreviewPanel.createFilePreviewPanel(file.getFileType());
			if(preview != null){
				preview.setCachedFile(file);											
				panelPreview = preview;				
			}
			else{
				panelPreview = new JPanel();	// empty panel
			}

			panelPreview.setBorder(new EmptyBorder(5, 5, 5, 5));
			add(panelPreview, BorderLayout.CENTER);
			invalidate();
		}		
	}

} 

