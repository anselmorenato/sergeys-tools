package org.sergeys.webcachedigger.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.Settings;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWinTopPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;		

	private WebCacheDigger wcd;
	
	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public MainWinTopPanel(WebCacheDigger wcd) throws IOException {
		
		this.wcd = wcd;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblBrowsers = new JLabel("Search in:");
		GridBagConstraints gbc_lblBrowsers = new GridBagConstraints();
		gbc_lblBrowsers.anchor = GridBagConstraints.EAST;
		gbc_lblBrowsers.insets = new Insets(0, 0, 5, 5);
		gbc_lblBrowsers.gridx = 0;
		gbc_lblBrowsers.gridy = 0;
		add(lblBrowsers, gbc_lblBrowsers);
		
		JPanel panelBrowsers = new JPanel();
		GridBagConstraints gbc_panelBrowsers = new GridBagConstraints();
		gbc_panelBrowsers.anchor = GridBagConstraints.WEST;
		gbc_panelBrowsers.insets = new Insets(0, 0, 5, 0);
		gbc_panelBrowsers.gridx = 1;
		gbc_panelBrowsers.gridy = 0;
		add(panelBrowsers, gbc_panelBrowsers);
		
//		JToggleButton tglbtnFirefox = new JToggleButton("Firefox");
//		tglbtnFirefox.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				doToggleStateChanged(e);
//			}
//		});
//		tglbtnFirefox.setIcon(new ImageIcon(MainWinTopPanel.class.getResource("/images/firefox.png")));		
//		panelBrowsers.add(tglbtnFirefox);
//		
//		JLabel lblSingleBrowser1 = new JLabel("Firefox");
//		lblSingleBrowser1.setIcon(new ImageIcon(MainWinTopPanel.class.getResource("/images/firefox.png")));
//		panelBrowsers.add(lblSingleBrowser1);
				
		LinkedHashSet<IBrowser> browsers = wcd.getExistingBrowsers(); 
				
		if(browsers.size() == 1){
			// do not offer any choice
			IBrowser b = browsers.iterator().next();
			JLabel lblSingleBrowser = new JLabel(b.getScreenName());
			lblSingleBrowser.setIcon(b.getIcon());
			panelBrowsers.add(lblSingleBrowser);
			wcd.getSettings().getActiveBrowsers().clear();
			wcd.getSettings().getActiveBrowsers().add(b.getName());
		}
		else{
			// add buttons for existing browsers
			for(IBrowser browser: browsers){
				JToggleButton toggle = new JToggleButton(browser.getScreenName());
				toggle.setName(browser.getName());
				toggle.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						doToggleStateChanged(e);
					}
				});
				toggle.setIcon(browser.getIcon());
				
				if(wcd.getSettings().getActiveBrowsers().contains(browser.getName())){
					toggle.setSelected(true);
				}
				
				panelBrowsers.add(toggle);			
			}
		}
		
		JLabel lblMedia = new JLabel("Search for:");
		GridBagConstraints gbc_lblMedia = new GridBagConstraints();
		gbc_lblMedia.anchor = GridBagConstraints.EAST;
		gbc_lblMedia.insets = new Insets(0, 0, 5, 5);
		gbc_lblMedia.gridx = 0;
		gbc_lblMedia.gridy = 1;
		add(lblMedia, gbc_lblMedia);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		add(panel_1, gbc_panel_1);
		
		JToggleButton tglbtnAudio = new JToggleButton("Audio");
		
		panel_1.add(tglbtnAudio);
		
		JToggleButton tglbtnVideo = new JToggleButton("Video");
		
		panel_1.add(tglbtnVideo);
		
		JToggleButton tglbtnImages = new JToggleButton("Images");
		
		panel_1.add(tglbtnImages);
		
		JToggleButton tglbtnOther = new JToggleButton("Other");
		
		panel_1.add(tglbtnOther);
		
		JLabel lblLargeThan = new JLabel("Large than");
		GridBagConstraints gbc_lblLargeThan = new GridBagConstraints();
		gbc_lblLargeThan.anchor = GridBagConstraints.EAST;
		gbc_lblLargeThan.insets = new Insets(0, 0, 0, 5);
		gbc_lblLargeThan.gridx = 0;
		gbc_lblLargeThan.gridy = 2;
		add(lblLargeThan, gbc_lblLargeThan);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 2;
		add(panel_2, gbc_panel_2);
		
		textField = new JTextField();
		textField.setText("100000");
		panel_2.add(textField);
		textField.setColumns(10);
		
		JLabel lblBytes = new JLabel("bytes");
		panel_2.add(lblBytes);

	}

	protected void doToggleStateChanged(ChangeEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		String name = btn.getName();	// browser name
		try {
			if(btn.isSelected()){				
				wcd.getSettings().getActiveBrowsers().add(name);				
			}
			else{
				wcd.getSettings().getActiveBrowsers().remove(name);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}				
	}

	public void init(HashSet<IBrowser> availableBrowsers){
		
	}
	
	
}
