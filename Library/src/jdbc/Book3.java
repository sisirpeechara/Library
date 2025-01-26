package jdbc;
import java.sql.*;
public class Book3 
{
    public void registerBorrower(int id,String name,String email,String phno)
    {
    	try
    	{
    	DBConnect.getConnect();
    	PreparedStatement ps=DBConnect.con.prepareStatement("insert into borrowers values(?,?,?,?)");
    	ps.setInt(1, id);
    	ps.setString(2, name);
    	ps.setString(3, email);
    	ps.setString(4, phno);
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
