package dicomviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbConnector {

	public static Connection getConnection() throws Exception {
		Class.forName("org.postgresql.Driver"); // Register
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DicomDB", "postgres",
				"postgres"); // Open Connection
		return conn;
	}
}
