package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.sergeys.library.swing.DirTreePanel;

public class DirSelectorDialog extends JDialog {

	public static final String DIRECTORY_SELECTED = "DIRECTORY_SELECTED"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	DirTreePanel dirTreePanel;
	
	/**
	 * Create the dialog.
	 */
	public DirSelectorDialog(Window owner) {
		super(owner);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle(Messages.getString("DirSelectorDialog.SelectDirectory")); //$NON-NLS-1$
		setIconImage(Toolkit.getDefaultToolkit().getImage(DirSelectorDialog.class.getResource("/images/icon.png"))); //$NON-NLS-1$
		setBounds(100, 100, 450, 462);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			dirTreePanel = new DirTreePanel(File.listRoots(), null, Messages.getString("DirSelectorDialog.MyMachine"), Messages.getString("DirSelectorDialog.MyHomeFolder")); //$NON-NLS-1$ //$NON-NLS-2$
			contentPanel.add(dirTreePanel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(Messages.getString("DirSelectorDialog.OK")); //$NON-NLS-1$
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doOK();
					}
				});
				okButton.setActionCommand("OK"); //$NON-NLS-1$
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(Messages.getString("DirSelectorDialog.Cancel")); //$NON-NLS-1$
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doCancel();
					}
				});
				cancelButton.setActionCommand("Cancel"); //$NON-NLS-1$
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void doCancel() {		
		setVisible(false);
	}

	protected void doOK() {
		File selectedDir = dirTreePanel.getSelectedDirectory();
		if(selectedDir != null){
			firePropertyChange(DIRECTORY_SELECTED, null, selectedDir.getAbsolutePath());
		}
		setVisible(false);
	}

}
