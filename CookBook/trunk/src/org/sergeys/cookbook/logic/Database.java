package org.sergeys.cookbook.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    
    public void addRecipe(String hash, File jarfile, String title){
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
			while(rs.next()){
				//System.out.println("inserted " + rs.getObject(1));
				System.out.println("inserted " + rs.getLong(1));
			}
			
			pst.close();							
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
