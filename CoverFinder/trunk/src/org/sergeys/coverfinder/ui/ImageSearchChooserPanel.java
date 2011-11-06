package org.sergeys.coverfinder.ui;

import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ServiceLoader;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.sergeys.coverfinder.logic.IImageSearchEngine;
import org.sergeys.coverfinder.logic.Settings;

public class ImageSearchChooserPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ButtonGroup bg;
	Hashtable<String, IImageSearchEngine> enginesByName = new Hashtable<String, IImageSearchEngine>();
	Hashtable<String, IImageSearchEngine> enginesByDispName = new Hashtable<String, IImageSearchEngine>();
	
	/**
	 * Create the panel.
	 */
	public ImageSearchChooserPanel() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		JLabel lblImageSearchMethod = new JLabel("Image search method:");
		add(lblImageSearchMethod);
		
		bg = new ButtonGroup();
		
		// designer
//		JRadioButton rdbtnBing = new JRadioButton("Bing");
//		add(rdbtnBing);
//		
//		JRadioButton rdbtnGoogle = new JRadioButton("Google");
//		add(rdbtnGoogle);
//				
//		bg.add(rdbtnBing);
//		bg.add(rdbtnGoogle);
		
		// runtime
		ServiceLoader<IImageSearchEngine> ldr = ServiceLoader.load(IImageSearchEngine.class);		
		for(IImageSearchEngine engine: ldr){
			enginesByName.put(engine.getName(), engine);
			enginesByDispName.put(engine.getDisplayName(), engine);
			
			JRadioButton rdbtn = new JRadioButton(engine.getDisplayName());

			bg.add(rdbtn);
			add(rdbtn);
		}
	}

	public void initValues(){
		IImageSearchEngine currentEngine = enginesByName.get(Settings.getInstance().getSearchEngineName());
		if(currentEngine != null){
			for(Enumeration<AbstractButton> en = bg.getElements(); en.hasMoreElements();){
				AbstractButton b = en.nextElement();
				if(b.getText().equals(currentEngine.getDisplayName())){
					b.setSelected(true);
					break;
				}
			}
		}
		else{
			bg.getElements().nextElement().setSelected(true);
		}
	}
	
	public void updateValues(){
		for(Enumeration<AbstractButton> en = bg.getElements(); en.hasMoreElements();){
			AbstractButton b = en.nextElement();
			if(b.isSelected()){
				Settings.getInstance().setSearchEngineName(enginesByDispName.get(b.getText()).getName());
				break;
			}
		}
	}
}
