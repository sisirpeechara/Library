package jdbc;
import java.sql.PreparedStatement;

public class Book1 
{
	 public void updateBook(int bkid,int qty)
	    {
	    	try
	    	{
	    	DBConnect.getConnect();
	    	PreparedStatement ps=DBConnect.con.prepareStatement("update  books set quantity=? where book_id=?");
	    	ps.setInt(1, qty);
	    	ps.setInt(2,bkid );
	    	
	    	int a=ps.executeUpdate();
	    	if(a>0)
	    	{
	    		System.out.println("Record Updated");
	    	}
	    	else
	    	{
	    		System.out.println("Record not Updated");
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
