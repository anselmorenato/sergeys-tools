package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.xml.rpc.ServiceException;

import org.apache.axis.types.UnsignedInt;
import org.sergeys.library.swing.ScaledImage;

import com.microsoft.schemas.LiveSearch._2008._03.Search.BingPortType;
import com.microsoft.schemas.LiveSearch._2008._03.Search.BingServiceLocator;
import com.microsoft.schemas.LiveSearch._2008._03.Search.ImageRequest;
import com.microsoft.schemas.LiveSearch._2008._03.Search.ImageResult;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchRequest;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchRequestType1;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchResponse;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchResponseType0;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SourceType;
import com.microsoft.schemas.LiveSearch._2008._03.Search.Thumbnail;

import java.awt.FlowLayout;

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
	private JLabel lblPlaceholder;
	private JLabel lblPh;
	
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
		txtQuery.setText("led zeppelin physical graffiti");
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
		panelCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblPlaceholder = new JLabel("placeholder");
		panelCenter.add(lblPlaceholder);
		
		lblPh = new JLabel("ph2");
		panelCenter.add(lblPh);
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
		
		ImageRequest ir = new ImageRequest();
		//ir.setCount(new UnsignedInt(15));
		ir.setOffset(new UnsignedInt(0));
		ir.setFilters(new String[]{ "Size:Medium", "Aspect:Square" });	// http://msdn.microsoft.com/en-us/library/dd560913.aspx
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
				
				panelCenter.removeAll();
				
				boolean found = false;
				//int i = 0;
				Image img = null;
				//while(!found){
				for(int i = 0; i < images.length; i++){
					ImageResult imgResult = images[i]; 
					
					String url = imgResult.getMediaUrl();
					Thumbnail th = imgResult.getThumbnail();
					
					try{
						//img = ImageIO.read(new URL(url));
						img = ImageIO.read(new URL(th.getUrl()));
						
						found = true;
						System.out.println(url + " found");
						
						ScaledImage scaledImage = new ScaledImage(img, false);
						
						scaledImage.setPreferredSize(new Dimension(100, 100));
						scaledImage.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
						
						
						panelCenter.add(scaledImage);
						scaledImage.invalidate();
						panelCenter.invalidate();
						panelCenter.repaint();
					}
					catch(Exception ex){
						System.out.println(url + " " + ex.getMessage());						
					}
					
				}
								
				
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
