package org.sergeys.privoxytool.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private static final String FILENAME = "privoxytool";

    private static Object instanceLock = new Object();
    private static Database instance;

    // singleton
    private Database() throws Exception
    {
        upgradeOrCreateIfNeeded();
    }

    public static Database getInstance() throws Exception
    {
        synchronized (instanceLock) {
            if(instance == null){
                instance = new Database();
            }
        }

        return instance;
    }

    private Connection connection;

    protected Connection getConnection() throws SQLException
    {
        if(connection == null || connection.isClosed()){
            String url = String.format("jdbc:h2:%s/%s", Settings.getSettingsDirPath(), Database.FILENAME).replace('\\', '/');
            //String url = String.format("jdbc:h2:%s/%s;JMX=TRUE", Settings.getSettingsDirPath(), Database.FILENAME).replace('\\', '/');
            connection = DriverManager.getConnection(url, "sa", "sa");
        }

        return connection;
    }

    private void upgrade() throws SQLException, IOException
    {
        Statement st = null;
        ResultSet rs = null;
        InputStream in = null;
        try {
            st = getConnection().createStatement();
            rs = st.executeQuery("select val from properties where property='version'");
            rs.next();
            String version = rs.getString("val");
            //rs.close();
            int ver = Integer.valueOf(version);

            // apply all existing upgrades
            in = getClass().getResourceAsStream("/resources/upgrade" + ver + ".sql");
            while(in != null){
                RunScript.execute(getConnection(), new InputStreamReader(in));
                in.close();
                Settings.getLogger().info("Upgraded database from version " + ver);
                ver++;
                in = getClass().getResourceAsStream("/resources/upgrade" + ver + ".sql");
            }

            //st.close();
        } catch (SQLException | IOException e) {
            Settings.getLogger().error("failed to upgrade db", e);
        }
        finally{
            if(in != null){
                in.close();
            }
            if(rs != null){
                rs.close();
            }
            if(st != null){
                st.close();
            }
        }
    }

    private void upgradeOrCreateIfNeeded() throws Exception {

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

        } catch (SQLException | IOException e) {
            throw e;
        }
        finally{
            conn.close();
        }
    }

    public void importLogfile(File logfile){
    	try {
    		
    		PreparedStatement psInsert = getConnection().prepareStatement(
                    "insert into privoxylog (tstamp, message, eventtype, fileurl, domain, secondleveldomain) " +
    				"values (?, ?, ?, ?, ?, ?)");
    		
			BufferedReader br = new BufferedReader(new FileReader(logfile));
			String line;
			while((line = br.readLine()) != null){
				// 2013-01-24 22:02:34.082 000009f8 Request: ic.pics.livejournal.com/queenbathory/48894231/8854/8854_original.jpg
				String[] tokens = line.split(" ");
			}
		} catch (IOException | SQLException ex) {
			Settings.getLogger().error("failed to import log file", ex);
		} 
    }
}
