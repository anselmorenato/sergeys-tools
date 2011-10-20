package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
			
	private Connection connection;
	
	protected Connection getConnection() throws SQLException{
		if(connection == null || connection.isClosed()){		
			String url = String.format("jdbc:h2:%s/%s", Settings.getSettingsDirPath(), Database.FILENAME).replace('\\', '/');
			connection = DriverManager.getConnection(url, "sa", "sa"); 
		}
		
		return connection;
	}
	
	private void createIfNeeded() throws SQLException {
		
		File dir = new File(Settings.getSettingsDirPath());
		if(!dir.exists()){
			dir.mkdirs();
		}

		Connection conn = getConnection(); 
		 
		try {
			// check whether table Properties exist
			ResultSet rs = conn.getMetaData().getTables(conn.getCatalog(), "PUBLIC", "PROPERTIES", null); // table names are uppercase
	
			if(rs.next()){
				// db exists, can upgrade here is needed
			}
			else{
				// create new structure
				InputStream in = getClass().getResourceAsStream("/resources/createdb.sql");
				RunScript.execute(conn, new InputStreamReader(in));
			}
		
		} catch (SQLException e) {
			throw e;
		}	
		finally{
			conn.close();
		}		
	}
			
	public void clearSaved() throws SQLException{
		Statement st = getConnection().createStatement();
		st.execute("update files set issaved = false");
		st.close();
	}
	
	// TODO: delete all which not exist on filesystem
	public void compact(){
		
	}
	
	public long countSaved() throws SQLException{
		long result = 0;
		
		Statement st = getConnection().createStatement();
		ResultSet rs = st.executeQuery("select count(id) from files where issaved = true");
		if(rs.next()){
			result = rs.getLong(1);
		}
		st.close();
		
		return result;
	}


	/**
	 * Exclude saved files with the same absolute path and timestamp 
	 * 
	 * @param cachedFiles
	 * @return
	 */
	public ArrayList<CachedFile> filterSavedByFilesystem(ArrayList<CachedFile> cachedFiles) {
		ArrayList<CachedFile> filtered = new ArrayList<CachedFile>();
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(
					"select id from files where absolutepath = ? and lastmodified = ? and filesize = ? and issaved");
			
			for(CachedFile file: cachedFiles){
				pst.setString(1, file.getAbsolutePath());
				pst.setLong(2, file.lastModified());
				pst.setLong(3, file.length());
				
				ResultSet rs = pst.executeQuery();				
				if(rs.next()){
					// has this file
//					SimpleLogger.logMessage("already has saved " + file.getAbsolutePath());
				}
				else{
					filtered.add(file);
				}								
			}
			
			pst.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return cachedFiles;
		}
		
		return filtered;
	}

	/**
	 * Populate unchangedFiles with hash and mime if possible.
	 * Remove preloaded files from allFiles. 
	 */
	public void preloadMetadata(ArrayList<CachedFile> allFiles, ArrayList<CachedFile> unchangedFiles) {
		//ArrayList<CachedFile> unknownFiles = new ArrayList<CachedFile>();
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(
					"select mimetype, hash from files where absolutepath = ? and lastmodified = ?");
			
			for(CachedFile file: allFiles){
				pst.setString(1, file.getAbsolutePath());
				pst.setLong(2, file.lastModified());
				
				ResultSet rs = pst.executeQuery();				
				if(rs.next()){
					// has this file
					//SimpleLogger.logMessage("already has mime for " + file.getAbsolutePath());
					
					file.setMimeType(rs.getString("mimetype"));
					file.setHash(rs.getString("hash"));
					
					unchangedFiles.add(file);
				}
				else{
					//unknownFiles.add(file);
				}
												
			}
			
			pst.close();

			allFiles.removeAll(unchangedFiles);			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public ArrayList<CachedFile> filterSavedByHash(ArrayList<CachedFile> cacheFiles) {
		ArrayList<CachedFile> filtered = new ArrayList<CachedFile>();
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(
					"select id from files where hash = ? and issaved");
			
			for(CachedFile file: cacheFiles){
				pst.setString(1, file.getHash());
								
				ResultSet rs = pst.executeQuery();				
				if(rs.next()){
					// has this file
//					SimpleLogger.logMessage("already has saved by hash " + file.getAbsolutePath());
				}
				else{
					filtered.add(file);
				}								
			}
			
			pst.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return cacheFiles;
		}
		
		return filtered;
	}


	public void insertUpdateMimeTypes(ArrayList<CachedFile> cacheFiles) throws SQLException {
		PreparedStatement psSelect = getConnection().prepareStatement(
				//"select id from files where absolutepath = ? and lastmodified = ?");	// got unique violation during insert
				"select id from files where absolutepath = ?");
		PreparedStatement psUpdate = getConnection().prepareStatement(
				"update files set mimetype = ?, lastmodified = ? where id = ?");
		PreparedStatement psInsert = getConnection().prepareStatement(
				"insert into files (absolutepath, lastmodified, filesize, mimetype)" +
				" values (?, ?, ?, ?)");
		
		getConnection().setAutoCommit(false);
		
		for(CachedFile file: cacheFiles){
			psSelect.setString(1, file.getAbsolutePath());			
			//psSelect.setLong(2, file.lastModified());
			//psSelect.setLong(3, file.length());
			ResultSet rs = psSelect.executeQuery();
			if(rs.next()){
				psUpdate.setString(1, file.getMimeType());
				psUpdate.setLong(2, file.lastModified());
				psUpdate.setLong(3, rs.getLong("id"));
				
				psUpdate.addBatch();
			}
			else{
				psInsert.setString(1, file.getAbsolutePath());
				psInsert.setLong(2, file.lastModified());
				psInsert.setLong(3, file.length());
				psInsert.setString(4, file.getMimeType());
				
				psInsert.addBatch();
			}
		}

		@SuppressWarnings("unused")
		int[] count = psInsert.executeBatch();
		count = psUpdate.executeBatch();
		getConnection().commit();
		
		psInsert.close();
		psUpdate.close();
		
		getConnection().setAutoCommit(true);
	}
	
	public void insertUpdateHashes(ArrayList<CachedFile> cacheFiles) throws SQLException {
				
		PreparedStatement psSelect = getConnection().prepareStatement(
				//"select id from files where absolutepath = ? and lastmodified = ?");
				"select id from files where absolutepath = ?");
		PreparedStatement psUpdate = getConnection().prepareStatement(
				"update files set hash = ?, lastmodified = ? where id = ?");
		PreparedStatement psInsert = getConnection().prepareStatement(
				"insert into files (absolutepath, lastmodified, filesize, mimetype, hash)" +
				" values (?, ?, ?, ?, ?)");
		
		getConnection().setAutoCommit(false);
				
		for(CachedFile file: cacheFiles){
						
			if(file.getHash() == null || file.getHash().isEmpty()){
				SimpleLogger.logMessage("error, empty hash: " + file.getAbsolutePath());
				continue;
			}
			
			psSelect.setString(1, file.getAbsolutePath());
			//psSelect.setLong(2, file.lastModified());
			ResultSet rs = psSelect.executeQuery();
			if(rs.next()){
												
				psUpdate.setString(1, file.getHash());
				psUpdate.setLong(2, file.lastModified());
				psUpdate.setLong(3, rs.getLong("id"));
				
				psUpdate.addBatch();
			}
			else{
				psInsert.setString(1, file.getAbsolutePath());
				psInsert.setLong(2, file.lastModified());
				psInsert.setLong(3, file.length());
				psInsert.setString(4, file.getMimeType());
				psInsert.setString(5, file.getHash());
				
				psInsert.addBatch();
			}
			
		}

		@SuppressWarnings("unused")
		int[] count = psInsert.executeBatch();
		count = psUpdate.executeBatch();
		getConnection().commit();
		
		psInsert.close();
		psUpdate.close();
		
		getConnection().setAutoCommit(true);
	}

	public void updateSaved(Collection<CachedFile> collection) throws Exception {
		
		PreparedStatement psUpdate = getConnection().prepareStatement(
				"update files set issaved = true where hash = ?");		
		
		getConnection().setAutoCommit(false);
				
		
		for(CachedFile file: collection){
			if(file.getHash() == null || file.getHash().isEmpty()){
				throw new Exception("no hash: " + file.getAbsolutePath());
			}
			psUpdate.setString(1, file.getHash());			
			psUpdate.addBatch();			
		}

		@SuppressWarnings("unused")
		int[] count = psUpdate.executeBatch();
		getConnection().commit();
		
		psUpdate.close();
		
		getConnection().setAutoCommit(true);
	}
	
	public ArrayList<File> getNotSavedFiles() throws SQLException{
		ArrayList<File> files = new ArrayList<File>();
		
		Statement st = getConnection().createStatement();
		ResultSet rs = st.executeQuery("select absolutepath from files where issaved = false");
		while(rs.next()){
			files.add(new File(rs.getString("absolutepath")));
		}
		
		return files;
	}

	public void removeByName(List<File> files) {
//		if(files == null){
//			return;
//		}
		
		try {
			
			getConnection().setAutoCommit(false);
			
			PreparedStatement pst = getConnection().prepareStatement("delete from files where absolutepath = ?");
			for(File file: files){
				pst.setString(1, file.getAbsolutePath());
				pst.addBatch();
			}
			
			int[] count = pst.executeBatch();
			getConnection().commit();
			
			pst.close();
			
			int total = 0;
			for(int i = 0; i < count.length; i++){
				total += count[i];
			}
			SimpleLogger.logMessage("removed nonexistent files: " + total);
			
			getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
