package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.h2.tools.RunScript;

public class Database {

	private static final String FILENAME = "wcd";
	
	private static Database instance;
	
	// singleton
	private Database() throws SQLException{
		createIfNeeded();
	}
	

	public static Database getInstance() throws SQLException{
		if(instance == null){
			instance = new Database();
		}
		
		return instance;
	}
	
	//private Settings settings;
	//private Connection conn;
	
//	public void setSettings(Settings settings){
//		this.settings = settings;
//	}
	
	public void shutdown(){
		
	}
	
	private Connection connection;
	
	protected Connection getConnection() throws SQLException{
		if(connection == null || connection.isClosed()){		
			String url = String.format("jdbc:h2:%s/%s", Settings.getSettingsDirPath(), Database.FILENAME).replace('\\', '/');
			connection = DriverManager.getConnection(url); 
		}
		
		return connection;
	}
	
	private void createIfNeeded() throws SQLException {
		Connection conn = getConnection(); 
		 
		try {
			ResultSet rs = conn.getMetaData().getTables(conn.getCatalog(), "PUBLIC", "PROPERTIES", null); // table names are uppercase
	
			if(rs.next()){
				// db exists, can upgrade here is needed
			}
			else{
				// create new
				InputStream in = getClass().getResourceAsStream("/resources/createdb.sql");
				RunScript.execute(conn, new InputStreamReader(in));
			}
		
		} catch (SQLException e) {
			throw e;
		}	
		finally{
			conn.close(); // TODO: do we need to close connection?
		}		
	}
	
	private boolean areSameSizeSaved(File file) throws SQLException{
		// check only by size
		boolean result = false;
		
		PreparedStatement pst = getConnection().prepareStatement("select count(id) from savedfiles where filesize = ?");
		pst.setLong(1, file.length());
		ResultSet rs = pst.executeQuery();
		if(rs.next()){
			result = rs.getLong(1) > 0;
		}
		
		pst.close();
		
		return result;
	}

	public boolean isSaved(CachedFile file) throws SQLException, NoSuchAlgorithmException, IOException{
		// check by size and hash
		boolean result = false;
		
		if(areSameSizeSaved(file)){
		
			PreparedStatement pst = getConnection().prepareStatement("select count(id) from savedfiles where filesize = ? and md5hash = ?");
			pst.setLong(1, file.length());
			pst.setString(2, file.getHash());
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				result = rs.getLong(1) > 0;
			}
			
			pst.close();
		}
		
		return result;		
	}		
	
	public void setSaved(Collection<CachedFile> files) throws SQLException, NoSuchAlgorithmException, IOException{
		

		getConnection().setAutoCommit(false);
		
		PreparedStatement pst = getConnection().prepareStatement(
				"insert into savedfiles (filename, filesize, md5hash) values (?, ?, ?)");
		
		for(CachedFile file: files){
			pst.setString(1, file.getName());
			pst.setLong(2, file.length());
			pst.setString(3, file.getHash());
			
//			SimpleLogger.logMessage(String.format(": %s %d %s", file.getName(), file.length(), file.getHash()));
			
			pst.addBatch();
		}
		
		@SuppressWarnings("unused")
		int[] count = pst.executeBatch();
		
		getConnection().commit();
		pst.close();
		
		getConnection().setAutoCommit(true);
		
	}
	
	public void clear() throws SQLException{
		Statement st = getConnection().createStatement();
		st.execute("truncate table savedfiles");
		st.close();
	}
	
	public long countSaved() throws SQLException{
		long result = 0;
		
		Statement st = getConnection().createStatement();
		ResultSet rs = st.executeQuery("select count(id) from savedfiles");
		if(rs.next()){
			result = rs.getLong(1);
		}
		st.close();
		
		return result;
	}
}
