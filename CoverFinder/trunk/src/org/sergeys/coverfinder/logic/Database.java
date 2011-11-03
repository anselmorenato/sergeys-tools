package org.sergeys.coverfinder.logic;

import java.io.File;
import java.io.IOException;
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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.h2.tools.RunScript;

public class Database {
	private static final String FILENAME = "coverfinder";
	
	private static Database instance;
	
	// singleton
	private Database() throws SQLException{
		upgradeOrCreateIfNeeded();
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
	
	private void upgrade(){
		Statement st;
		try {
			st = getConnection().createStatement();
			ResultSet rs = st.executeQuery("select val from properties where property='version'");
			rs.next();
			String version = rs.getString("val");
			int ver = Integer.valueOf(version);
			
			// apply all existing upgrades
			InputStream in = getClass().getResourceAsStream("/resources/upgrade"+ver+".sql");
			while(in != null){
				RunScript.execute(getConnection(), new InputStreamReader(in));
				in.close();
				System.out.println("Upgraded database from version " + ver);
				ver++;
				in = getClass().getResourceAsStream("/resources/upgrade"+ver+".sql");
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void upgradeOrCreateIfNeeded() throws SQLException {
		
		File dir = new File(Settings.getSettingsDirPath());
		if(!dir.exists()){
			dir.mkdirs();
		}

		Connection conn = getConnection(); 
		 
		try {
			// check whether table Properties exist
			ResultSet rs = conn.getMetaData().getTables(conn.getCatalog(), "PUBLIC", "PROPERTIES", null); // table names are uppercase
	
			if(!rs.next()){			
				// create new structure
				InputStream in = getClass().getResourceAsStream("/resources/createdb.sql");
				RunScript.execute(conn, new InputStreamReader(in));
			}
			
			// apply upgrades
			upgrade();
		
		} catch (SQLException e) {
			throw e;
		}	
		finally{
			conn.close();
		}		
	}

	/**
	 * Returns changed files
	 * 
	 * @param files
	 * @return
	 */
	public Collection<Track> filterUnchanged(Collection<Track> files){
		ArrayList<Track> changed = new ArrayList<Track>();
		
		try {
			PreparedStatement pst = getConnection().prepareStatement(
					"select id from files where absolutepath = ? and lastmodified = ? and filesize = ?");
			
			for(Track file: files){
				pst.setString(1, file.getFile().getAbsolutePath());
				pst.setLong(2, file.getFile().lastModified());
				pst.setLong(3, file.getFile().length());
				
				ResultSet rs = pst.executeQuery();				
				if(rs.next()){
					// has this file
//					SimpleLogger.logMessage("already has saved " + file.getAbsolutePath());
				}
				else{
					changed.add(file);
				}								
			}
			
			pst.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return changed;
	}
	
	public void insertOrUpdate(Collection<Track> files) throws SQLException{
		PreparedStatement psSelect = getConnection().prepareStatement(				
				"select id from files where absolutepath = ?");
		PreparedStatement psUpdate = getConnection().prepareStatement(
				"update files set haspicture = ?, hash = ?, lastmodified = ? where id = ?");
		PreparedStatement psInsert = getConnection().prepareStatement(
				"insert into files (absolutepath, absolutedir, lastmodified, filesize, mimetype," +
						" detectionmethod, hash, haspicture, album, artist)" +
				" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		getConnection().setAutoCommit(false);
				
		
		HashSet<String> unique = new HashSet<String>();
		
		for(Track file: files){
						
			if(unique.contains(file.getFile().getAbsolutePath())){
				System.out.println("Already inserted/updated: " + file.getFile().getAbsolutePath());
				continue;
			}
			else{
				unique.add(file.getFile().getAbsolutePath());
			}
			
//			if(file.getHash() == null || file.getHash().isEmpty()){
//				SimpleLogger.logMessage("error, empty hash: " + file.getAbsolutePath());
//				continue;
//			}
			
			psSelect.setString(1, file.getFile().getAbsolutePath());

			ResultSet rs = psSelect.executeQuery();
			if(rs.next()){
				psUpdate.setBoolean(1, true);						
				psUpdate.setString(2, file.getHash());
				psUpdate.setLong(3, file.getFile().lastModified());
				psUpdate.setLong(4, rs.getLong("id"));
				
				psUpdate.addBatch();
			}
			else{
				psInsert.setString(1, file.getFile().getAbsolutePath());
				psInsert.setString(2, file.getFile().getParentFile().getAbsolutePath());
				psInsert.setLong(3, file.getFile().lastModified());								
				psInsert.setLong(4, file.getFile().length());				
				psInsert.setString(5, file.getMimeType());
				psInsert.setString(6, file.getDetectFilesMethod().toString());
				psInsert.setString(7, file.getHash());
				psInsert.setBoolean(8, file.isHasPicture());
				psInsert.setString(9, file.getAlbum());
				psInsert.setString(10, file.getArtist());
				
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
	
	public Collection<Track> selectTracks(Album.HasCover hasCover) throws SQLException{
		ArrayList<Track> tracks = new ArrayList<Track>();
		
		Statement st = getConnection().createStatement();
		ResultSet rs = st.executeQuery("select absolutepath from files");
		while(rs.next()){
			Track track = new Track(new File(rs.getString("absolutepath")));
			tracks.add(track);
		}
		
		return tracks;
	}


	public void deleteByAbsolutePath(List<File> list) {
		// TODO Auto-generated method stub
		
	}


	public List<File> selectAllFiles() {
		// TODO Auto-generated method stub
		return null;
	}
}
