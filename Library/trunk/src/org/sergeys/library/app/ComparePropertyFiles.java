package org.sergeys.library.app;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class ComparePropertyFiles {
	
	// TODO: add arguments to automatically add new props and remove unused props from target files
	// TODO: accept more target files
	
	public static void main(String[] args) {
		
		if(args.length != 2){
			System.out.println("Two files are required");		
			return;
		}
		
				
		Properties p1 = new Properties();
		try {
			p1.load(new FileInputStream(args[0]));
		} catch (Exception e) {
			System.out.println(String.format("Cannot open %s: %s", args[0], e.getMessage()) );
			return;
		}
		
		Properties p2 = new Properties();
		try {
			p2.load(new FileInputStream(args[1]));
		} catch (Exception e) {
			System.out.println(String.format("Cannot open %s: %s", args[1], e.getMessage()) );
			return;
		}
		
		System.out.println(String.format("Properties only in %s:", args[0]));
		System.out.println();
		compareProps(p1, p2);
		System.out.println();
		
		System.out.println(String.format("Properties only in %s:", args[1]));
		System.out.println();
		compareProps(p2, p1);
	}
	
	public static void compareProps(Properties p1, Properties p2){
		
		@SuppressWarnings("unchecked")
		Set<Object> keys = ((Hashtable<Object, Object>) p1.clone()).keySet(); 
		keys.removeAll(p2.keySet());
		
		for(Object o: keys){
			System.out.println(String.format("%s=%s", o, p1.get(o)));
		}
	}

}
