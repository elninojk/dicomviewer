package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector
{
	static String driverClass = "org.postgresql.Driver";
	static String url = "jdbc:postgresql://localhost:5432/dcmdb";
	private static DbConnector jdbc;
	
	private DbConnector()
	{
		
	}
	 public static DbConnector getInstance() {    
         if (jdbc==null)  
       {  
                jdbc=new DbConnector();  
       }  
         return jdbc;  
	 }  
	 
	public  Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName(driverClass);
		Connection con = DriverManager.getConnection(url, "postgres", "postgres");
		return con;
	}
}

