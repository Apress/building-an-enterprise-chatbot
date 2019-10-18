package com.iris.bot.states;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.State;

public class GetClaimStatus extends State {
	/*
	 *  Java Database Connectivity (JDBC) is an application programming interface (API) for the programming language Java, which defines how a client may access a database. It is a Java-based data access technology used for Java database connectivity. It is part of the Java Standard Edition platform, from Oracle Corporation
	 *  DB_URL is the datbase connection URL. 
	 *  The URL used is dependent upon the particular database and JDBC driver. It will always begin with the "jdbc:" protocol, but the rest is up to the particular vendor.
	 *  In our example we have used mysql database
	 *  
	 */
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	// database name is test
	static final String DB_URL = "jdbc:mysql://localhost/test";

	/*
	 *  Database access credentials. 
	 */
	static final String USERNAME = "ClaimsReadOnlyUser";
	static final String PASSWORD = "**********";

	public GetClaimStatus() {
		super("getClaimStatus");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		Connection conn = null;
		Statement stmt = null;
		String status = null;
		/*
		 * retreive claim Id from session or from slot of the matched intent.
		 * If this state is executed,it is supposed to mean that we have the claim Id otherwise Shield would nothave validated transition to this state
		 */
		String claimId = SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "claimId", null);

		// Default answer
		String answer = "We dont have any info related to " + claimId + " in our system.\n"
				+ "Please contact our call service representative for further enquiry on the number 1800 333 0333 between Mondays to Fridays, 8:30 am to 5:30 pm.\n"
				+ "If you're dialling from overseas or via a payphone, please call +65 633 30333.\nIs there anything else I can help you with?";
		try {
			//Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection (Connecting to database...)
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

			// Execute a query
			stmt = conn.createStatement();
			/*
			 * SQL query to query row from test database and claims table
			 * This query means - return status of the row from claims table where claim id is given claim Id
			 */
			String sql = "SELECT status FROM claims where claimId='" + claimId + "'";

			//executing SQL
			ResultSet rs = stmt.executeQuery(sql);

			//Extract data from result set
			while (rs.next()) {
				//record fetched
				status = rs.getString("status");
			}

			//Clean-up environment and close active connections
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*
			 * In a try-catch, finally block is always executed even if any exception happens. In case of exceptions in the above code for any reason,
			 * the statements and connections will not get closed. Hence we apply an extra check to close it in finally block
			 */
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// if we received status from database for that claims, we override the default answer with actual status details
		if (status != null) {
			answer = "The status of your claim for claimId " + claimId + " is  - " + status
					+ ".\nContact our representatives at HELPLINE-NUMBER "
					+ "between Mondays to Fridays, 8:30am to 5:30pm if you want to enquire more. Anything else that you want to know as of now?";
		}
		// remove claim Id related attributes from session
		session.removeAttribute("getclaimidprompt");
		session.removeAttribute("claimid");
		return answer;
	}

}
