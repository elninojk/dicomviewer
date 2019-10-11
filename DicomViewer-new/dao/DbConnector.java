package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector 
{
	static String driverClass = "org.postgresql.Driver";
	static String url = "jdbc:postgresql://localhost:5432/dcmdb";
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName(driverClass);
		Connection con = DriverManager.getConnection(url, "postgres", "postgres");
		return con;
	}
}
