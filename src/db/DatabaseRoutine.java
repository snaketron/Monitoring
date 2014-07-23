package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.ReentrantLock;

import utils.GlobalConst;
import msg.DataMsg;
import msg.NetworkMsg;

/**
 * This class (routine) is used to establish a connection to a existent sqlite 
 * database and use this connection to insert data and network messages to the
 * tables data_t and network_t of that database.
 */
public class DatabaseRoutine {
	private static ReentrantLock dataLock = new ReentrantLock();
	private static ReentrantLock netLock = new ReentrantLock();
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Establish connection to the database and return the connection object for 
	 * the purpose of inserting data later on.
	 */
	public static Connection connectToDb() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + GlobalConst.DB);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}
		return conn;
	}
	
	/**
	 * Insert data message into data_t table
	 */
	public static void insertDataMsg(Connection conn, DataMsg dm) {
		dataLock.lock();
		synchronized (conn) {
			try {	 
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement("INSERT INTO " + GlobalConst.DATA_TABLE 
						+ " VALUES (?, ?, ?, ?, ?, ?, ?)");
				
				ps.setInt(2, dm.getOrigin());
				ps.setDouble(3, dm.getHumidity());
				ps.setDouble(4, dm.getTemperature());
				ps.setDouble(5, dm.getVolt());
				ps.setDouble(6, dm.getLight());
				
				String d = df.format(dm.getSecond().getStart());
				ps.setString(7, d);
				
				ps.executeUpdate();
				ps.close();
				conn.commit();
			} 
			catch(Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				dataLock.unlock();
			}
		}
		dataLock.unlock();
	}
	
	/**
	 * Insert network msg into network_t table.
	 */
	public static void insertNetworkMsg(Connection conn, NetworkMsg nm) {
		netLock.lock();
		synchronized (conn) {
			try {	 
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement("INSERT INTO " + 
						GlobalConst.NETWORK_TABLE  + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				
				ps.setInt(2, nm.getOrigin());
				ps.setDouble(3, nm.getParent());
				ps.setBoolean(4, nm.getConnectivity() > 0);		
				ps.setInt(5, nm.getHops());
				ps.setInt(6, nm.getConnectedSince());
				ps.setInt(7, nm.getMeanRSSI());
				String d = df.format(nm.getTime().getStart());
				ps.setString(8, d);
				
				ps.executeUpdate();
				ps.close();
				conn.commit();
			} 
			catch(Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
				netLock.unlock();
			}
		}
		netLock.unlock();
	}
}