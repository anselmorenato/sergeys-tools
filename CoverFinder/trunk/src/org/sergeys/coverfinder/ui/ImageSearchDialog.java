package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.sergeys.coverfinder.logic.IImageSearchEngine;
import org.sergeys.coverfinder.logic.IProgressWatcher;
import org.sergeys.coverfinder.logic.ImageSearchRequest;
import org.sergeys.coverfinder.logic.ImageSearchResult;
import org.sergeys.coverfinder.logic.ImageSearchWorker;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.library.swing.DisabledPanel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class ImageSearchDialog 
extends JDialog 
implements IProgressWatcher<ImageSearchResult> 
{

	@Override
	public void setVisible(boolean b) {						
		super.setVisible(b);
		
		//doSearch();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DisabledPanel dContentPanel;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldQuery;
	JLabel lblProgress;
	JPanel panelResults;

	/**
	 * Create the dialog.
	 */
	public ImageSearchDialog(Window owner) {
		super(owner);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		setTitle("Search images");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImageSearchDialog.class.getResource("/images/icon.png")));
		setBounds(100, 100, 593, 414);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		dContentPanel = new DisabledPanel(contentPanel);

		JComponent glass = dContentPanel.getGlassPane();
		glass.setLayout(new BorderLayout());
		JLabel lblProgress = new JLabel();
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgress.setIcon(new ImageIcon(ImageSearchDialog.class.getResource("/images/progress.gif")));
		glass.add(lblProgress, BorderLayout.CENTER);
		
		//getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(dContentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelQuery = new JPanel();
			FlowLayout fl_panelQuery = (FlowLayout) panelQuery.getLayout();
			fl_panelQuery.setAlignment(FlowLayout.LEFT);
			contentPanel.add(panelQuery, BorderLayout.NORTH);
			
			{
				textFieldQuery = new JTextField();
				textFieldQuery.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						doKeyTyped(e);
					}
				});
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
//			{
//				lblProgress = new JLabel("");
//				lblProgress.setIcon(new ImageIcon(ImageSearchDialog.class.getResource("/images/progress.gif")));
//				lblProgress.setVisible(false);
//				panelQuery.add(lblProgress);
//			}
		}
		{
			JScrollPane scrollPane = new JScrollPane(); 
			panelResults = new JPanel();
			scrollPane.setViewportView(panelResults);
			panelResults.setLayout(new GridLayout(0, 5, 5, 5));
			contentPanel.add(scrollPane, BorderLayout.CENTER);
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

	public void setQuery(String query){
		textFieldQuery.setText(query);
		queryStringChanged = true;
	}
	
	private boolean queryStringChanged = false;
	
	protected void doKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		queryStringChanged = true;
	}


	protected void doSearchMore() {
		dContentPanel.setEnabled(false);
		
	}


	protected void doSearch() {
		//lblProgress.setVisible(true);
		
		String query = textFieldQuery.getText();
		if(!query.isEmpty()){
								
			ImageSearchRequest req = new ImageSearchRequest();
			req.setQuery(query);
			IImageSearchEngine engine = Settings.getInstance().getImageSearchEngine();
			ImageSearchWorker wrk = new ImageSearchWorker(engine, req, false, this);
			
			panelResults.removeAll();
			dContentPanel.setEnabled(false);
			
			wrk.execute();		
		}
	}


	protected void doClose() {
		setVisible(false);		
	}


	@Override
	public void updateStage(IProgressWatcher.Stage stage) {
	}


	@Override
	public void updateProgress(long count, IProgressWatcher.Stage stage) {		
	}


	@Override
	public void progressComplete(Collection<ImageSearchResult> items, IProgressWatcher.Stage stage) {
		
		dContentPanel.setEnabled(true);
		
		for(ImageSearchResult res: items){
			Image img = null;
			try {
				img = ImageIO.read(res.getThumbnailUrl());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResultImagePanel resPanel = new ResultImagePanel(img, res.getWidth(), res.getHeight(), res.getFileSize()); 
			panelResults.add(resPanel);
		}										
	}


	@Override
	public boolean isAllowedToContinue(IProgressWatcher.Stage stage) {
		return true;
	}

}
