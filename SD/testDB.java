import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class testDB {
	public static void main(String argv[]) throws ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://163.13.201.32/2014score?useunicode=true&characterencoding=utf 8";
		String user = "test";
		String password = "1234";
		try(Connection conn = DriverManager.getConnection(jdbcurl,user,password)){
			System.out.println("success");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
