package NBA;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.*;

public class NBA_PLAYER {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String jdbcurl = "jdbc:mysql://163.13.201.32/player?useunicode=true&characterencoding=utf 8";
		String user = "test";
		String password = "1234";
		URL url = new URL(
				"http://www.nbastuffer.com/2012-2013_NBA_Regular_Season_Player_Stats.html");
		Document doc = Jsoup.parse(url, 30000);
		Elements stat = doc.select("#PLAYER tr td");
		int i = 0;
		StringBuilder sb = new StringBuilder();
		String data;
		try {

			{
				PreparedStatement pstmt = null;
				while (i != -1) {
					
					/*System.out.print(stat.get(i).text());
					if(i%25==0){
						System.out.println();
						
					}*/
					if(i%25!=0)
						sb.append(stat.get(i).text() + "&");
					i += 1;
				}

			}
		} catch (Exception e) {
			i = -1;
		}
		try (Connection conn = DriverManager.getConnection(jdbcurl, user,
				password)) {
			System.out.printf("資料庫已%s %n", conn.isClosed() ? "關閉" : "開-起");
			Statement stmt = conn.createStatement();
			
			PreparedStatement pstmt = null;
			System.out.println(sb);
			createTable("2012_13_NBA_Regular_Season"  , conn ,stmt, pstmt );
			insertIntoDB("2012_13_NBA_Regular_Season" , sb.toString().split("&") , conn , pstmt);
			conn.close();
			pstmt.close();
		}

	}

	public static void insertIntoDB(String name ,String[] data, Connection conn,
			PreparedStatement pstmt) throws SQLException {

		int p = 0;
		while (p < data.length) {
			pstmt = conn
					.prepareStatement("INSERT INTO "+name+" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < 24; i++) {
				pstmt.setString(i + 1, data[p]);
				p++;
			}
			pstmt.executeUpdate();

		}

		System.out.println("done");
	}
	public static void createTable(String name,  Connection conn,
			Statement stmt, PreparedStatement pstmt) throws SQLException {
		String sql = "CREATE TABLE " + name + " (`PLAYER` VARCHAR(255), " + "`TEAM` VARCHAR(255) , " 
				+ "`POS` VARCHAR(255), " + "`AGE` VARCHAR(255), "
				+ "`GP` VARCHAR(255), " + "`MPG` VARCHAR(255), "
				+ "`MIN%` VARCHAR(255), " + "`USG%` VARCHAR(255), "
				+ "`TOr` VARCHAR(255), " + "`FTA` VARCHAR(255), "
				+ "`FT%`VARCHAR(255), " + "`2PA` VARCHAR(255), "
				+ "`2P%` VARCHAR(255), " + "`3PA` VARCHAR(255),"
				+ "`3P%` VARCHAR(255), " + "`TS%` VARCHAR(255), "
				+ "`PPG` VARCHAR(255)," + "`RPG` VARCHAR(255) , "
				+ "`TRB%` VARCHAR(255)," + "`APG` VARCHAR(255) , "
				+ "`AST%` VARCHAR(255)," + "`SPG` VARCHAR(255) , "
				+ "`BRB%` VARCHAR(255)," + "`VI` VARCHAR(255)) "
				+ "ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin";

		// ///////建立UTF8編碼資料表

		stmt.executeUpdate(sql);



	}

}