package jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnect 
{
    public static Connection con;
    public static void getConnect () throws Exception
    {
    	try
    	{
    	//1. Loading a driver class
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	System.out.println("Driver Loaded");
    	//2. establish a connection
    	con=DriverManager.getConnection("jdbc:mysql://localhost:3306/lms","root","tiger");
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
