package jdbc;
import java.sql.PreparedStatement;

public class Book5
{
	 public void deleteBorrower(int id)
	    {
	    	try
	    	{
	    	DBConnect.getConnect();
	    	PreparedStatement ps=DBConnect.con.prepareStatement("delete from  borrowers where borrower_id=?");
	    	ps.setInt(1,id );
	    	
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
