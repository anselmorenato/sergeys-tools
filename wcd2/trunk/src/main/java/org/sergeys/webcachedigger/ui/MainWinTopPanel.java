package org.sergeys.webcachedigger.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.LinkedHashSet;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.sergeys.webcachedigger.logic.IBrowser;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Settings;

public class MainWinTopPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public MainWinTopPanel(WebCacheDigger wcd) throws IOException {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblBrowsers = new JLabel(Messages.getString("MainWinTopPanel.SearchIn")); //$NON-NLS-1$
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
				
		// browsers
		
		LinkedHashSet<IBrowser> browsers = wcd.getExistingBrowsers(); 
				
		if(browsers.size() == 0){
			JLabel lblNoBrowsers = new JLabel(Messages.getString("MainWinTopPanel.NoBrowsersDetected")); //$NON-NLS-1$
			panelBrowsers.add(lblNoBrowsers);
		}
		else if(browsers.size() == 1){
			// do not offer any choice
			IBrowser b = browsers.iterator().next();
			JLabel lblSingleBrowser = new JLabel(b.getScreenName());
			lblSingleBrowser.setIcon(b.getIcon());
			panelBrowsers.add(lblSingleBrowser);
			Settings.getInstance().getActiveBrowsers().clear();
			Settings.getInstance().getActiveBrowsers().add(b.getName());
		}		
		else{
			// add buttons for existing browsers
			for(IBrowser browser: browsers){
				JToggleButton toggle = new JToggleButton(browser.getScreenName());
				toggle.setName(browser.getName());
				toggle.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						doToggleBrowser(e);
					}
				});
				toggle.setIcon(browser.getIcon());
				
				if(Settings.getInstance().getActiveBrowsers().contains(browser.getName())){
					toggle.setSelected(true);
				}
				
				panelBrowsers.add(toggle);			
			}
		}
		
		JLabel lblMedia = new JLabel(Messages.getString("MainWinTopPanel.SearchFor")); //$NON-NLS-1$
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
		
		// file types
		
		JToggleButton tglbtnAudio = new JToggleButton(Messages.getString("MainWinTopPanel.Audio")); //$NON-NLS-1$
		tglbtnAudio.setIcon(new ImageIcon(MainWinTopPanel.class.getResource("/images/midi.png"))); //$NON-NLS-1$
		tglbtnAudio.setName(Settings.FileType.Audio.name());
		tglbtnAudio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				doToggleMedia(e);
			}
		});
		tglbtnAudio.setSelected(Settings.getInstance().getActiveFileTypes().contains(Settings.FileType.Audio));
		panel_1.add(tglbtnAudio);
		
		JToggleButton tglbtnVideo = new JToggleButton(Messages.getString("MainWinTopPanel.Video")); //$NON-NLS-1$
		tglbtnVideo.setIcon(new ImageIcon(MainWinTopPanel.class.getResource("/images/video.png"))); //$NON-NLS-1$
		tglbtnVideo.setName(Settings.FileType.Video.name());
		tglbtnVideo.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				doToggleMedia(e);
			}
		});
		tglbtnVideo.setSelected(Settings.getInstance().getActiveFileTypes().contains(Settings.FileType.Video));
		panel_1.add(tglbtnVideo);
		
		JToggleButton tglbtnImages = new JToggleButton(Messages.getString("MainWinTopPanel.Images")); //$NON-NLS-1$
		tglbtnImages.setIcon(new ImageIcon(MainWinTopPanel.class.getResource("/images/image.png"))); //$NON-NLS-1$
		tglbtnImages.setName(Settings.FileType.Image.name());
		tglbtnImages.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				doToggleMedia(e);
			}
		});
		tglbtnImages.setSelected(Settings.getInstance().getActiveFileTypes().contains(Settings.FileType.Image));
		panel_1.add(tglbtnImages);
		
		JToggleButton tglbtnOther = new JToggleButton(Messages.getString("MainWinTopPanel.Other")); //$NON-NLS-1$
		tglbtnOther.setIcon(new ImageIcon(MainWinTopPanel.class.getResource("/images/kmultiple.png"))); //$NON-NLS-1$
		tglbtnOther.setName(Settings.FileType.Other.name());
		tglbtnOther.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				doToggleMedia(e);
			}
		});
		tglbtnOther.setSelected(Settings.getInstance().getActiveFileTypes().contains(Settings.FileType.Other));
		panel_1.add(tglbtnOther);

	}

	protected void doToggleMedia(ChangeEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		String name = btn.getName();	// browser name
		Settings.FileType type = Enum.valueOf(Settings.FileType.class, name);
		if(btn.isSelected()){				
			Settings.getInstance().getActiveFileTypes().add(type);				
		}
		else{
			Settings.getInstance().getActiveFileTypes().remove(type);				
		}
		
	}

	protected void doToggleBrowser(ChangeEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		String name = btn.getName();	// browser name
		if(btn.isSelected()){				
			Settings.getInstance().getActiveBrowsers().add(name);				
		}
		else{
			Settings.getInstance().getActiveBrowsers().remove(name);
		}				
	}
	
}
