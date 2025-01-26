package jdbc;
import java.sql.PreparedStatement;

public class Book2
{
	 public void deleteBook(int bkid)
	    {
	    	try
	    	{
	    	DBConnect.getConnect();
	    	PreparedStatement ps=DBConnect.con.prepareStatement("delete from  books where book_id=?");
	    	ps.setInt(1,bkid );
	    	
	    	int a=ps.executeUpdate();
	    	if(a>0)
	    	{
	    		System.out.println("Record Deleted");
	    	}
	    	else
	    	{
	    		System.out.println("Record not Deleted");
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
