package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import org.sergeys.library.swing.DisabledPanel;

public class ImageSearchDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DisabledPanel dContentPanel;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldQuery;
	JLabel lblProgress;

	/**
	 * Create the dialog.
	 */
	public ImageSearchDialog(Window owner) {
		super(owner);
		
		setTitle("Search images");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImageSearchDialog.class.getResource("/images/icon.png")));
		setBounds(100, 100, 544, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		dContentPanel = new DisabledPanel(contentPanel);
				
		//getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(dContentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelQuery = new JPanel();
			FlowLayout fl_panelQuery = (FlowLayout) panelQuery.getLayout();
			fl_panelQuery.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panelQuery, BorderLayout.NORTH);
			{
				lblProgress = new JLabel("");
				lblProgress.setIcon(new ImageIcon(ImageSearchDialog.class.getResource("/images/progress.gif")));
				lblProgress.setVisible(false);
				panelQuery.add(lblProgress);
			}
			{
				textFieldQuery = new JTextField();
				panelQuery.add(textFieldQuery);
				textFieldQuery.setColumns(30);
			}
			{
				JButton btnSearch = new JButton("Search");
				btnSearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSearch();
					}
				});
				panelQuery.add(btnSearch);
			}
			{
				JButton btnMore = new JButton("More");
				btnMore.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSearchMore();
					}
				});
				panelQuery.add(btnMore);
			}
		}
		{
			JPanel panelResults = new JPanel();
			contentPanel.add(panelResults, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doClose();
					}
				});
				btnClose.setActionCommand("Cancel");
				buttonPane.add(btnClose);
			}
		}
	}


	protected void doSearchMore() {
		dContentPanel.setEnabled(false);
		
	}


	protected void doSearch() {
		lblProgress.setVisible(true);
		dContentPanel.setEnabled(false);		
	}


	protected void doClose() {
		setVisible(false);		
	}

}
