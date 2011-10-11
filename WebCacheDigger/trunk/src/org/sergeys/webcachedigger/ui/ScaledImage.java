package org.sergeys.webcachedigger.ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public class ScaledImage 
extends JComponent 
{

	Image img;
	
	public ScaledImage(Image img){
		this.img = img;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(img != null){

			int sw = img.getWidth(this);
			int sh = img.getHeight(this);
			int dw = this.getSize().width;
			int dh = this.getSize().height;
			
			float saspect = (float)sw / sh;
			float daspect = (float)dw / dh;
			
			int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
			
			float scale = 0;
			if(saspect > daspect){
				// landscape
				scale = (float)dw / sw;
				dx1 = 0;					
				dy1 = (int)((dh - (sh * scale)) / 2);
				dx2 = dw - 1;
				dy2 = dy1 + (int)(sh * scale);
				
			}
			else{
				// portrait
				scale = (float)dh / sh;
				dx1 = (int)((dw - (sw * scale)) / 2);
				dy1 = 0;
				dx2 = dx1 + (int)(sw * scale);
				dy2 = dh - 1;				
			}
			
			g.drawImage(img, 
					dx1, dy1, dx2, dy2, 
					0, 0, sw - 1, sh - 1, this);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
