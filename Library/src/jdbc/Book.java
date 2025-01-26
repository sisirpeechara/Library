package jdbc;
import java.sql.*;
public class Book 
{
    public void addBook(int bkid,String title,String publisher,String author,int qty)
    {
    	try
    	{
    	DBConnect.getConnect();
    	PreparedStatement ps=DBConnect.con.prepareStatement("insert into books values(?,?,?,?,?)");
    	ps.setInt(1, bkid);
    	ps.setString(2, title);
    	ps.setString(3, publisher);
    	ps.setString(4, author);
    	ps.setInt(5, qty);
    	int a=ps.executeUpdate();
    	if(a>0)
    	{
    		System.out.println("Record Inserted");
    	}
    	else
    	{
    		System.out.println("Record not Inserted");
    	}
    	ps.close();
    	DBConnect.con.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
