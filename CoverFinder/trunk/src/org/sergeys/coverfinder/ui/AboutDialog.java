package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.JTabbedPane;

import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.library.swing.SystemPropertiesTable;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JTextPane;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutDialog(Window parent) {
		super(parent);
		
		setTitle(Messages.getString("AboutDialog.AboutCoverFinder")); //$NON-NLS-1$
		setModalityType(ModalityType.APPLICATION_MODAL);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AboutDialog.class.getResource("/images/icon.png"))); //$NON-NLS-1$
		setModal(true);
		//setBounds(100, 100, 450, 300);
		this.setSize(500, 300);
		this.setPreferredSize(new Dimension(500, 300));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel panelAbout = new JPanel();
				tabbedPane.addTab(Messages.getString("AboutDialog.AboutTab"), null, panelAbout, null); //$NON-NLS-1$
				GridBagLayout gbl_panelAbout = new GridBagLayout();
				//gbl_panelAbout.columnWidths = new int[]{0, 0, 0};
				//gbl_panelAbout.rowHeights = new int[]{0, 0, 0, 0, 0};
				//gbl_panelAbout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				//gbl_panelAbout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panelAbout.setLayout(gbl_panelAbout);
				{
					JLabel lblTitle = new JLabel("Title"); //$NON-NLS-1$
					GridBagConstraints gbc_lblTitle = new GridBagConstraints();
					gbc_lblTitle.insets = new Insets(0, 0, 5, 0);
					gbc_lblTitle.gridx = 0;
					gbc_lblTitle.gridy = 0;
					panelAbout.add(lblTitle, gbc_lblTitle);
					lblTitle.setText("Cover Finder"); //$NON-NLS-1$
				}
				{
					JLabel lblVersion = new JLabel("Version"); //$NON-NLS-1$
					GridBagConstraints gbc_lblVersion = new GridBagConstraints();
					gbc_lblVersion.insets = new Insets(0, 0, 5, 0);
					gbc_lblVersion.gridx = 0;
					gbc_lblVersion.gridy = 1;
					panelAbout.add(lblVersion, gbc_lblVersion);
					lblVersion.setText(Settings.getInstance().getVersionDisplay());
				}
				{
					JLabel lblAuthor = new JLabel("Author"); //$NON-NLS-1$
					GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
					gbc_lblAuthor.insets = new Insets(0, 0, 5, 0);
					gbc_lblAuthor.gridx = 0;
					gbc_lblAuthor.gridy = 2;
					panelAbout.add(lblAuthor, gbc_lblAuthor);
					lblAuthor.setText(Messages.getString("AboutDialog.AuthorName")); //$NON-NLS-1$
				}
				{
					JLabel lblJavaruntime = new JLabel("JavaRuntime"); //$NON-NLS-1$
					GridBagConstraints gbc_lblJavaruntime = new GridBagConstraints();
					gbc_lblJavaruntime.gridx = 0;
					gbc_lblJavaruntime.gridy = 3;
					panelAbout.add(lblJavaruntime, gbc_lblJavaruntime);
					lblJavaruntime.setText(
							System.getProperties().getProperty("java.runtime.name") + " " +  //$NON-NLS-1$ //$NON-NLS-2$
							System.getProperties().getProperty("java.runtime.version")); //$NON-NLS-1$
				}
			}
			{
				JPanel panelSystem = new JPanel();
				tabbedPane.addTab(Messages.getString("AboutDialog.SystemTab"), null, panelSystem, null); //$NON-NLS-1$
				panelSystem.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane scrollPane = new JScrollPane();
					panelSystem.add(scrollPane, BorderLayout.CENTER);
				
				
					SystemPropertiesTable systemPropertiesTable = new SystemPropertiesTable(Messages.getString("AboutDialog.Property"), Messages.getString("AboutDialog.Value")); //$NON-NLS-1$ //$NON-NLS-2$
					//panelSystem.add(systemPropertiesTable, BorderLayout.NORTH);
					scrollPane.setViewportView(systemPropertiesTable);
				}
			}
			{
				JPanel panelLibraries = new JPanel();
				tabbedPane.addTab(Messages.getString("AboutDialog.LibrariesTab"), null, panelLibraries, null); //$NON-NLS-1$
				panelLibraries.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane scrollPane = new JScrollPane();
					panelLibraries.add(scrollPane, BorderLayout.CENTER);
					{
						JTextPane txtpnLibraries = new JTextPane();
						txtpnLibraries.setText("Libraries"); //$NON-NLS-1$
						scrollPane.setViewportView(txtpnLibraries);
						
						txtpnLibraries.addHyperlinkListener(new HyperlinkListener() {
							public void hyperlinkUpdate(HyperlinkEvent e) {
								doHyperlinkUpdate(e);
							}				
						});
						txtpnLibraries.setEditable(false);

						try {
							txtpnLibraries.setPage(AboutDialog.class.getResource("/resources/libraries.html")); //$NON-NLS-1$
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(Messages.getString("AboutDialog.OK"));				 //$NON-NLS-1$
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doOK();
					}
				});
				okButton.setActionCommand("OK"); //$NON-NLS-1$
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	protected void doHyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
			//SimpleLogger.logMessage(e.getURL().toString());
			
			if(Desktop.isDesktopSupported()){
				Desktop dt = Desktop.getDesktop();
				if(dt.isSupported(Desktop.Action.BROWSE)){
					try {
						dt.browse(e.getURL().toURI());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}		
	}

	protected void doOK() {
		setVisible(false);		
	}

}
