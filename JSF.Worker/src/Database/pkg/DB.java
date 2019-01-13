package Database.pkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import Objects.pkg.Worker;

@ManagedBean(name="dbb")
@SessionScoped
public class DB {

	private Connection conn = null;
	private Statement st = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private String msg = "";
	private static DB db_ton = null;

	public void setMsg(String msg) { this.msg = msg; }
	public String getMsg() { return this.msg; }

	private DB() {}

	public static DB getDBBClass() { if(db_ton==null) db_ton = new DB(); return db_ton; }

	@SuppressWarnings("finally")
	public Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@HOSTNAME:PORT:SID","username","pass");
			conn.setAutoCommit(false);
		} catch (Exception e) { e.printStackTrace(); System.out.println("Unable to load");
		} finally { return conn; }
	}

	public void breakConnection() throws SQLException{ conn.close(); st.close(); rs.close(); }
	public void commitTransaction() throws SQLException{ conn.commit(); }
	public void rollbackTransaction() throws SQLException{ conn.rollback(); }

	//Workers
	@SuppressWarnings("finally")
	public ArrayList<Worker> getWorkers() throws SQLException{

		ArrayList<Worker> list_workers = new ArrayList<Worker>();

		try {
			conn = getConnection(); st = conn.createStatement(); rs = st.executeQuery("select * from radnik");

			while(rs.next()) { 
				Worker r = new Worker();

				r.setPersonsID(rs.getString("jmbg"));
				r.setName(rs.getString("ime"));
				r.setLastName(rs.getString("prezime"));

				list_workers.add(r); 
			}
		} catch (Exception e) { e.getMessage();
		} finally { breakConnection(); return list_workers; }
	}

	public boolean insWorkerDB(Worker worker) {

		try { setMsg("");
			conn = getConnection();
			ps = conn.prepareStatement("insert into radnik values (?, ?, ?)");

			ps.setString(1, worker.getPersonsID());
			ps.setString(2, worker.getName());
			ps.setString(3, worker.getLastName());

			ps.executeUpdate();
			return true;

		} catch (SQLException e) { setMsg(e.getMessage()); System.err.println(e.getMessage()); return false; } 
	}

	public boolean updWorkerDB(Worker worker) {
		try { setMsg("");
			conn = getConnection();
			ps = conn.prepareStatement("update radnik set ime = ?, prezime = ? where jmbg = ?");

			ps.setString(1, worker.getName());
			ps.setString(2, worker.getLastName());
			ps.setString(3, worker.getPersonsID());

			ps.executeUpdate();
			return true;

		} catch (SQLException e) { setMsg(e.getMessage()); System.err.println(e.getMessage()); return false; }
	}

	public boolean delWorkerDB(Worker worker) {

		try { setMsg("");
			conn = this.getConnection();
			ps = conn.prepareStatement("delete from radnik where JMBG = ?");

			ps.setString(1, worker.getPersonsID());

			ps.executeUpdate();
			return true; 

		} catch (SQLException e) { setMsg(e.getMessage()); System.err.println(e.getMessage()); return false; }
	}

	
}

