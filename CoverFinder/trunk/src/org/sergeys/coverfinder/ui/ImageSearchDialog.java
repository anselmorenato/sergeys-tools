package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.sergeys.coverfinder.logic.IImageSearchEngine;
import org.sergeys.coverfinder.logic.IProgressWatcher;
import org.sergeys.coverfinder.logic.ImageSearchRequest;
import org.sergeys.coverfinder.logic.ImageSearchResult;
import org.sergeys.coverfinder.logic.ImageSearchWorker;
import org.sergeys.coverfinder.logic.MusicItem;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.coverfinder.ui.ImageDetailsDialog.EditImageEvent;
import org.sergeys.library.swing.DisabledPanel;

public class ImageSearchDialog 
extends JDialog 
implements IProgressWatcher<ImageSearchResult>, PropertyChangeListener, ActionListener
{

	@Override
	public void setVisible(boolean b) {
		if(b){
			panelResults.removeAll();
			lblBranding.setText(Settings.getInstance().getImageSearchEngine().getBranding());
		}
		
		super.setVisible(b);						
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
	MusicItem musicItem;
	ActionListener actionListener;
	
	/**
	 * Create the dialog.
	 */
	public ImageSearchDialog(Window owner, MusicItem musicItem, ActionListener actionListener) {
		super(owner);
		
		this.musicItem = musicItem;
		this.actionListener = actionListener;
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		setTitle(Messages.getString("ImageSearchDialog.SearchImages")); //$NON-NLS-1$
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImageSearchDialog.class.getResource("/images/icon.png"))); //$NON-NLS-1$
		setBounds(100, 100, 593, 414);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		dContentPanel = new DisabledPanel(contentPanel);

		JComponent glass = dContentPanel.getGlassPane();
		glass.setLayout(new BorderLayout());
		JLabel lblProgress = new JLabel();
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgress.setIcon(new ImageIcon(ImageSearchDialog.class.getResource("/images/progress.gif"))); //$NON-NLS-1$
		glass.add(lblProgress, BorderLayout.CENTER);
		
		//getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(dContentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelQuery = new JPanel();
			FlowLayout fl_panelQuery = (FlowLayout) panelQuery.getLayout();
			fl_panelQuery.setAlignment(FlowLayout.LEFT);
			
			JPanel panelBrand = new JPanel();
			
			JPanel panelTop = new JPanel();
			panelTop.setLayout(new BorderLayout(0, 0));
			panelTop.add(panelQuery, BorderLayout.CENTER);
			panelTop.add(panelBrand, BorderLayout.SOUTH);
			
			lblBranding = new JLabel("branding"); //$NON-NLS-1$
			panelBrand.add(lblBranding);
			
			
			//contentPanel.add(panelQuery, BorderLayout.NORTH);
			contentPanel.add(panelTop, BorderLayout.NORTH);
			
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
				JButton btnSearch = new JButton(Messages.getString("ImageSearchDialog.Search")); //$NON-NLS-1$
				btnSearch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSearch();
					}
				});
				panelQuery.add(btnSearch);
			}
			{
				btnMore = new JButton(Messages.getString("ImageSearchDialog.More")); //$NON-NLS-1$
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
				JButton btnClose = new JButton(Messages.getString("ImageSearchDialog.Close")); //$NON-NLS-1$
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doClose();
					}
				});
				btnClose.setActionCommand(Messages.getString("ImageSearchDialog.Cancel")); //$NON-NLS-1$
				buttonPane.add(btnClose);
			}
		}
		
		
	}

	private void setupControls(){
		btnMore.setEnabled(!queryStringChanged);
	}
	
	public void setQuery(String query, MusicItem musicItem){
		this.musicItem = musicItem;
		textFieldQuery.setText(query);
		queryStringChanged = true;
		setupControls();
	}
	
	private boolean queryStringChanged = false;
	
	protected void doKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		queryStringChanged = true;
		setupControls();
	}

	IImageSearchEngine currentEngine;
	private JButton btnMore;
	private JLabel lblBranding;

	protected void doSearch() {
		//lblProgress.setVisible(true);
		
		String query = textFieldQuery.getText();
		if(!query.isEmpty()){
								
			ImageSearchRequest req = new ImageSearchRequest();
			req.setQuery(query);
			currentEngine = Settings.getInstance().getImageSearchEngine();
			ImageSearchWorker wrk = new ImageSearchWorker(currentEngine, req, false, this);
			
			panelResults.removeAll();
			dContentPanel.setEnabled(false);
			
			wrk.execute();		
		}
	}

	protected void doSearchMore() {
		ImageSearchWorker wrk = new ImageSearchWorker(currentEngine, null, true, this);
		dContentPanel.setEnabled(false);
		wrk.execute();
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
//			Image img = null;
//			try {
//				img = ImageIO.read(res.getThumbnailUrl());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if(res.hasImages()){
				ResultImagePanel resPanel = new ResultImagePanel(res.getThumbnailImage(), res); 
				panelResults.add(resPanel);
				resPanel.addPropertyChangeListener(ResultImagePanel.SELECTED_IMAGE_PROPERTY, this);
			}
		}
		
		queryStringChanged = false;
		setupControls();
	}


	@Override
	public boolean isAllowedToContinue(IProgressWatcher.Stage stage) {
		return true;
	}

	@Override
	public void reportException(Throwable ex) {
		String message;
		if(ex.getCause() != null){
			message = String.format(Messages.getString("ImageSearchDialog.FailedToSearch2"), ex.getLocalizedMessage(), ex.getCause().getLocalizedMessage()); //$NON-NLS-1$
		}
		else{
			message = String.format(Messages.getString("ImageSearchDialog.FailedToSearch"), ex.getLocalizedMessage()); //$NON-NLS-1$
		}
		JOptionPane.showMessageDialog(this, message);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ImageSearchResult res = (ImageSearchResult)evt.getNewValue();
		ImageDetailsDialog dlg = new ImageDetailsDialog(this, res, musicItem);
		dlg.addActionListener(actionListener);
		dlg.addActionListener(this);
		dlg.setLocationRelativeTo(this);
		dlg.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		if(e instanceof EditImageEvent){
			this.setVisible(false);
		}
	}

}
