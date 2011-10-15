package org.sergeys.coverfinder.ui;

import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.xml.rpc.ServiceException;

import org.apache.axis.types.UnsignedInt;
import org.sergeys.library.swing.ScaledImage;

import com.microsoft.schemas.LiveSearch._2008._03.Search.BingPortType;
import com.microsoft.schemas.LiveSearch._2008._03.Search.BingServiceLocator;
import com.microsoft.schemas.LiveSearch._2008._03.Search.ImageRequest;
import com.microsoft.schemas.LiveSearch._2008._03.Search.ImageResult;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchOption;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchRequest;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchRequestType1;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchResponse;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchResponseType0;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SourceType;
import com.microsoft.schemas.LiveSearch._2008._03.Search.WebSearchOption;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

public class CoverFinder {

	// get wsdl from http://api.bing.net/search.wsdl?AppID=3835365F7AE679189D6105256B8EFE900B846E6A&Version=2.2 
	
	private static final String APP_ID = "3835365F7AE679189D6105256B8EFE900B846E6A";
	
	private JFrame frame;
	private JTextField txtQuery;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CoverFinder window = new CoverFinder();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CoverFinder() {
		initialize();
	}

	JPanel panelCenter;
	private JLabel label;
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		txtQuery = new JTextField();
		txtQuery.setText("cinquetti");
		panel.add(txtQuery);
		txtQuery.setColumns(30);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doSearch(e);
			}
		});
		panel.add(btnSearch);
		
		panelCenter = new JPanel();
		panelCenter.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		label = new JLabel("New label");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panelCenter.add(label);
	}

	protected void doSearch(ActionEvent e) {
		
		String query = txtQuery.getText();
		
		if(query.isEmpty()){
			return;
		}
		
		SearchRequest req = new SearchRequest();
		req.setAppId(APP_ID);
		req.setSources(new SourceType[]{ SourceType.Image });
		req.setQuery(query);
		//req.setOptions(new SearchOption[]{ new SearchOption("") });
		
		ImageRequest ir = new ImageRequest(new UnsignedInt(5), new UnsignedInt(5), null);
		//ir.setCount(new UnsignedInt(5));
		//ir.setOffset(new UnsignedInt(5));
		//ir.setFilters(new String[]{ "Size:Small", "Aspect:Tall" });	// http://msdn.microsoft.com/en-us/library/dd560913.aspx
		req.setImage(ir);
		
		BingServiceLocator loc = new BingServiceLocator();
		try {
			BingPortType portType = loc.getBingPort();
			SearchRequestType1 reqType = new SearchRequestType1(req);			
			SearchResponseType0 respType = portType.search(reqType);
			SearchResponse resp = respType.getParameters();
			
			ImageResult[] images = resp.getImage().getResults();
			if(images.length > 0){
				
				System.out.println(images.length + " results");
				
				boolean found = false;
				int i = 1;
				Image img = null;
				while(!found){
					ImageResult imgResult = images[i]; 
					
					String url = imgResult.getMediaUrl();
					
					try{
						img = ImageIO.read(new URL(url));
						found = true;
						System.out.println(url + " found");
					}
					catch(Exception ex){
						System.out.println(url + " " + ex.getMessage());
						i++;
					}
					
				}
								
				ScaledImage scaledImage = new ScaledImage(img, false);
				
				panelCenter.removeAll();
				panelCenter.add(scaledImage);
				scaledImage.invalidate();
				panelCenter.invalidate();
				panelCenter.repaint();
			}
			
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}

}
