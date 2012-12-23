package org.sergeys.cookbook.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import org.h2.tools.RunScript;

public class Database {
    private static final String FILENAME = "cookbook";

    private static Object instanceLock = new Object();
    private static Database instance;

    // singleton
    private Database() throws SQLException{
        upgradeOrCreateIfNeeded();
    }

    public static Database getInstance() throws SQLException{
        synchronized (instanceLock) {
            if(instance == null){
                instance = new Database();
            }
        }

        return instance;
    }

    private Connection connection;

    protected Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()){
            String url = String.format("jdbc:h2:%s/%s", Settings.getSettingsDirPath(), Database.FILENAME).replace('\\', '/');
            //String url = String.format("jdbc:h2:%s/%s;JMX=TRUE", Settings.getSettingsDirPath(), Database.FILENAME).replace('\\', '/');
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

            st.close();
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
                InputStream in = Database.class.getResourceAsStream("/resources/createdb.sql");
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

    public boolean isRecipeExists(String hash){
    	try {
			PreparedStatement pst = getConnection().prepareStatement("select id from recipes where hash = ?");
			pst.setString(1, hash);
			ResultSet rs = pst.executeQuery();
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    public long addRecipe(String hash, File jarfile, String title){
    	
    	long id = 0;
    	
    	try {
			PreparedStatement pst = getConnection().prepareStatement(
					"insert into recipes (hash, title, packedfile, filesize) " +
					"values (?, ?, ?, ?)");
			pst.setString(1, hash);
			pst.setString(2, title);
			InputStream is = new FileInputStream(jarfile);
			pst.setBinaryStream(3, is);
			pst.setLong(4, jarfile.length());
			
			pst.executeUpdate();
			
			is.close();
			
			ResultSet rs = pst.getGeneratedKeys();
//			ResultSetMetaData meta = rs.getMetaData();
//			int count = meta.getColumnCount();
//			while(rs.next()){			
//				System.out.println("inserted " + rs.getLong(1));
//			}
			
			if(rs.next()){
				id = rs.getLong(1);
			}
			
			pst.close();							
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    	
    	return id;
    }
    
    public ArrayList<Recipe> getAllRecipes(){    	
    	ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    	try {
			//PreparedStatement pst = getConnection().prepareStatement("select hash, title from recipes");
	        Statement st = getConnection().createStatement();
	        ResultSet rs = st.executeQuery("select hash, title from recipes");
	        while(rs.next()){
	        	Recipe r = new Recipe();
	        	r.setHash(rs.getString("hash"));
	        	r.setTitle(rs.getString("title"));
	        	recipes.add(r);	            
	        }

	        st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return recipes;
    }
    
    public void extractRecipeFile(String hash, File targetFile){
    	try {
			PreparedStatement pst = getConnection().prepareStatement("select packedfile from recipes where hash = ?");
			pst.setString(1, hash);
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				InputStream is = rs.getBinaryStream("packedfile");

				FileOutputStream fos = new FileOutputStream(targetFile);
				byte[] buf = new byte[20480];
				int count = is.read(buf);
				while(count > 0){
					fos.write(buf, 0, count);
					count = is.read(buf);
				}
				
				fos.close();
				is.close();
				
//				ReadableByteChannel ich = Channels.newChannel(is);
//				FileOutputStream fos = new FileOutputStream(targetFile);
//				
//				// magic number for Windows, 64Mb - 32Kb)
//				int maxCount = (64 * 1024 * 1024) - (32 * 1024);
//								
//				int available = is.available();	// returns zero here
//				long position = 0;
//				while(available > 0){
//					int count = (available > maxCount) ? maxCount: available;					
//					position += fos.getChannel().transferFrom(ich, position, count);
//					available = is.available();
//				}
//				
//				fos.close();
//				ich.close();
//				is.close();								
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
