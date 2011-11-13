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
import java.util.List;
import java.util.Set;

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
						" detectionmethod, hash, haspicture, album, artist, title)" +
				" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
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
				psInsert.setString(9, file.getAlbumTitle());
				psInsert.setString(10, file.getArtist());
				psInsert.setString(11, file.getTitle());
				
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
	
	/**
	 * 
	 * @param hasCover
	 * @param tagsLanguage if not null, tags are decoded
	 * @param paths if not null, only tracks from that paths returned
	 * @return
	 * @throws SQLException
	 */
	public Collection<Track> selectTracks(Album.HasCover hasCover, String tagsLanguage, Set<String> paths) throws SQLException{
		ArrayList<Track> tracks = new ArrayList<Track>();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select absolutepath, absolutedir, haspicture, album, artist, title from files");
		
//		switch(hasCover){
//		case NoTracks
//		
//		}
		
//		sb.append(" where haspicture = false");
		
		if(paths != null && !paths.isEmpty()){
			StringBuilder sbWhere = new StringBuilder();
			
			for(@SuppressWarnings("unused") String path: paths){
				if(sbWhere.length() > 0){
					sbWhere.append(" or ");
				}
				
				//sbWhere.append("instr(lower(absolutedir), ?) = 1");
				sbWhere.append("instr(absolutedir, ?) = 1");
			}
			
			sb.append(" where (");
			sb.append(sbWhere);
			sb.append(")");
		}
		
		sb.append(" order by artist, album");
//System.out.println(sb);		
		//Statement st = getConnection().createStatement();
		//ResultSet rs = st.executeQuery(sb.toString());
		
		PreparedStatement pst = getConnection().prepareStatement(sb.toString());
		if(paths != null && !paths.isEmpty()){
			int i = 1;
			for(String path: paths){
				//pst.setString(i++, path.toLowerCase());
				pst.setString(i++, path);
			}
		}
		ResultSet rs = pst.executeQuery();
		
		
		if(tagsLanguage != null){
			Mp3Utils.getInstance().setDecodeLanguage(tagsLanguage);
			Mp3Utils.getInstance().setDecodeStrings(true);
		}
		else{
			Mp3Utils.getInstance().setDecodeStrings(false);
		}
				
		while(rs.next()){
			Track track = new Track(new File(rs.getString("absolutepath")));
			track.setFilesystemDir(rs.getString("absolutedir"));
			track.setHasPicture(rs.getBoolean("haspicture"));
			String str = rs.getString("album");
			str = Mp3Utils.getInstance().decode(str);
			track.setAlbumTitle(str == null || str.isEmpty() ? "<unknown album>" : str);
			str = rs.getString("artist");
			str = Mp3Utils.getInstance().decode(str);
			track.setArtist(str == null || str.isEmpty() ? "<unknown artist>" : str);
			str = rs.getString("title");
			str = Mp3Utils.getInstance().decode(str);
			track.setTitle(str == null || str.isEmpty() ? "<unknown track>" : str);
			
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
